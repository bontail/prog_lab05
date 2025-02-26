package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.validator.InvalidField;
import main.java.dev.bontail.lab5.Invoker;
import main.java.dev.bontail.lab5.PersonManager;
import main.java.dev.bontail.lab5.validator.Validator;
import main.java.dev.bontail.lab5.data.Coordinates;
import main.java.dev.bontail.lab5.data.Location;
import main.java.dev.bontail.lab5.data.Person;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class AddCommand implements Command {
    PersonManager personManager;
    Invoker invoker;

    public AddCommand(PersonManager personManager, Invoker invoker) {
        this.personManager = personManager;
        this.invoker = invoker;
    }

    protected Person createPerson(String[] personArgs){
        Validator personValidator = new Validator(Person.class, false);
        InvalidField invalidField = personValidator.checkArgs(personArgs);
        while (invalidField != null) {
            System.out.println("Please write valid value for: " + invalidField.name());
            String[] new_args = this.invoker.getLineTokens();
            if (new_args.length != 1){
                System.out.println("Please write one value");
                continue;
            }

            personArgs[invalidField.index()] = new_args[0];
            invalidField = personValidator.checkArgs(personArgs);
        }

        Validator coordinatesValidator = new Validator(Coordinates.class, true);
        String[] coordinatesArgs = this.checkRecursiveClasses(personValidator, coordinatesValidator);

        Validator locationValidator = new Validator(Location.class, true);
        String[] locationArgs = this.checkRecursiveClasses(personValidator, locationValidator);

        Coordinates coordinates = (Coordinates) coordinatesValidator.createInstance(coordinatesArgs, new ArrayList<>());
        Location location = (Location) locationValidator.createInstance(locationArgs, new ArrayList<>());

        Object[] newPersonArgs = new Object[personArgs.length + 2];
        System.arraycopy(personArgs, 0, newPersonArgs, 0, personArgs.length);
        newPersonArgs[personArgs.length] = coordinates;
        newPersonArgs[personArgs.length + 1] = location;

        return (Person) personValidator.createInstance(newPersonArgs, new ArrayList<>(){{
            add(Coordinates.class);
            add(Location.class);
        }});
    }

    @Override
    public Boolean execute(String[] personArgs) {
        Validator personValidator = new Validator(Person.class, false);
        if (personArgs.length != personValidator.getCheckedFields().size()) {
            System.out.println("add need " + personValidator.getCheckedFields().size() + " fields [name, height, weight, hairColor, nationality]");
            return false;
        }

        Person person = this.createPerson(personArgs);
        this.personManager.addPerson(person);
        System.out.println("Successful add " + person.getName());

        return true;
    }

    private String[] checkRecursiveClasses(Validator mainValidator, Validator Validator) {
        InvalidField invalidField;
        ArrayList<String> args = new ArrayList<>();
        String fieldName = Validator.cls.getSimpleName();
        for (Field field: mainValidator.getNestedFields()){
            if (field.getType().equals(Validator.cls)){
                fieldName = field.getName();
                break;
            }
        }
        while (Validator.getCurrentFieldName() != null) {
            System.out.println("Please enter: " + fieldName + " " + Validator.getCurrentFieldName());
            String[] tokens = this.invoker.getLineTokens();
            if (tokens.length == 0){
                tokens = new String[1];
            }else if (tokens.length != 1) {
                tokens[0] = String.join(" ", args);
            }
            do {
                invalidField = Validator.checkArgs(tokens);
                if (invalidField != null) {
                    System.out.println("Please write valid value for: " + fieldName + " " + invalidField.name());
                    tokens = this.invoker.getLineTokens();
                    if (tokens.length == 0){
                        tokens = new String[1];
                    }else if (tokens.length != 1) {
                        tokens[0] = String.join(" ", args);
                    }
                }
            } while (invalidField != null);
            args.add(tokens[0]);
        }

        String[] newArgs = new String[args.size()];
        newArgs = args.toArray(newArgs);
        return newArgs;
    }


    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getHelpInfo() {
        return "Add a person to collection [name, height, weight, hairColor, nationality]";
    }
}
