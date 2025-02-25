package main.java.dev.bontail.lab5.commands;
import main.java.dev.bontail.lab5.InvalidField;
import main.java.dev.bontail.lab5.Invoker;
import main.java.dev.bontail.lab5.PersonManager;
import main.java.dev.bontail.lab5.ReflectionInvoker;
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
        ReflectionInvoker personReflectionInvoker = new ReflectionInvoker(Person.class, false);
        InvalidField invalidField = personReflectionInvoker.checkArgs(personArgs);
        while (invalidField != null) {
            System.out.println("Please write valid value for: " + invalidField.name());
            String[] new_args = this.invoker.getLineTokens();
            if (new_args.length != 1){
                System.out.println("Please write one value");
                continue;
            }

            personArgs[invalidField.index()] = new_args[0];
            invalidField = personReflectionInvoker.checkArgs(personArgs);
        }

        ReflectionInvoker coordinatesReflectionInvoker = new ReflectionInvoker(Coordinates.class, true);
        String[] coordinatesArgs = this.checkRecursiveClasses(personReflectionInvoker, coordinatesReflectionInvoker);

        ReflectionInvoker locationReflectionInvoker = new ReflectionInvoker(Location.class, true);
        String[] locationArgs = this.checkRecursiveClasses(personReflectionInvoker, locationReflectionInvoker);

        Coordinates coordinates = (Coordinates) coordinatesReflectionInvoker.createInstance(coordinatesArgs, new ArrayList<>());
        Location location = (Location) locationReflectionInvoker.createInstance(locationArgs, new ArrayList<>());

        Object[] newPersonArgs = new Object[personArgs.length + 2];
        System.arraycopy(personArgs, 0, newPersonArgs, 0, personArgs.length);
        newPersonArgs[personArgs.length] = coordinates;
        newPersonArgs[personArgs.length + 1] = location;

        return (Person) personReflectionInvoker.createInstance(newPersonArgs, new ArrayList<>(){{
            add(Coordinates.class);
            add(Location.class);
        }});
    }

    @Override
    public Boolean execute(String[] personArgs) {
        ReflectionInvoker personReflectionInvoker = new ReflectionInvoker(Person.class, false);
        if (personArgs.length != personReflectionInvoker.getCheckedFields().size()) {
            System.out.println("add need " + personReflectionInvoker.getCheckedFields().size() + " fields [name, height, weight, hairColor, nationality]");
            return false;
        }

        Person person = this.createPerson(personArgs);
        this.personManager.addPerson(person);
        System.out.println("Successfully added " + person.getName());

        return true;
    }

    private String[] checkRecursiveClasses(ReflectionInvoker mainReflectionInvoker, ReflectionInvoker reflectionInvoker) {
        InvalidField invalidField;
        ArrayList<String> args = new ArrayList<>();
        String fieldName = reflectionInvoker.cls.getSimpleName();
        for (Field field: mainReflectionInvoker.getRecursiveFields()){
            if (field.getType().equals(reflectionInvoker.cls)){
                fieldName = field.getName();
                break;
            }
        }
        while (reflectionInvoker.getCurrentFieldName() != null) {
            System.out.println("Please enter: " + fieldName + " " + reflectionInvoker.getCurrentFieldName());
            String[] tokens = this.invoker.getLineTokens();
            if (tokens.length == 0){
                tokens = new String[1];
            }else if (tokens.length != 1) {
                tokens[0] = String.join(" ", args);
            }
            do {
                invalidField = reflectionInvoker.checkArg(tokens[0]);
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
