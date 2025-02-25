package main.java.dev.bontail.lab5;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import main.java.dev.bontail.lab5.data.Person;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PersonManager {
    private ArrayDeque<Person> persons = new ArrayDeque<Person>();
    final public java.util.Date creationDate;
    final private static String dumpFilename = ".dump.csv";

    public PersonManager() {
        this.creationDate = new java.util.Date();
        this.read();
    }

    public boolean isActiveDump(){
        ArrayList<Person> dumpPersonsArray = this.read(dumpFilename);
        ArrayList<Person> currentPersonsArray = new ArrayList<>(this.persons);
        assert dumpPersonsArray != null;
        return !(dumpPersonsArray.containsAll(currentPersonsArray) && currentPersonsArray.containsAll(dumpPersonsArray));
    }

    public void dump(){
        this.write(dumpFilename);
    }

    public void setFromDump(){
        this.persons.clear();
        this.persons.addAll(this.read(dumpFilename));
        System.out.println("Success set from dump");
    }

    private ArrayList<Person> read(String fileName) {
        try{
            Reader myReader = new FileReader(fileName, StandardCharsets.UTF_8);
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = mapper.schemaFor(Person.class)
                    .withColumnSeparator(';').withSkipFirstDataRow(true);
            MappingIterator<Person> iterator = mapper
                    .readerFor(Person.class)
                    .with(schema)
                    .readValues(myReader);
            ArrayList<Person> personsArray = (ArrayList<Person>) iterator.readAll();
            Collections.sort(personsArray);
            HashSet<Integer> ids = new HashSet<>();
            for (Person person : personsArray) {
                boolean isAdded = ids.add(person.getId());
                Person.updateLastId(person.getId());
                if (!isAdded) {
                    System.out.println("Data contains non unique id: " + person.getId());
                    return null;
                }
            }
            return personsArray;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void read(){
        String fileName = System.getenv("COLLECTION_FILENAME");
        if (fileName == null) {
            System.out.println("No environment variable COLLECTION_FILENAME found");
            return;
        }

        this.persons.addAll(this.read(fileName));
    }

    private void write(String fileName) {
        try {
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = mapper.schemaFor(Person.class)
                    .withColumnSeparator(';')
                    .withHeader();
            ObjectWriter writer = mapper.writer(schema);
            writer.writeValue(new FileWriter(fileName, StandardCharsets.UTF_8), this.persons);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void write() {
        String fileName = System.getenv("COLLECTION_FILENAME");
        if (fileName == null) {
            System.out.println("No environment variable COLLECTION_FILENAME found");
            return;
        }
        this.write(fileName);
    }


    public void addPerson(Person person) {
        this.persons.addLast(person);
        Person[] personsArray = this.persons.toArray(new Person[0]);
        Arrays.sort(personsArray);
        this.persons.clear();
        this.persons.addAll(Arrays.asList(personsArray));
        this.dump();
    }

    public void removePerson(Person person) {
        this.persons.removeFirstOccurrence(person);
        this.dump();
    }

    public Stream<Person> getFilteredPersons(Predicate<Person> predicate) {
        return this.persons.stream().filter(predicate);
    }

    public Person getPersonByField(Predicate<Person> predicate) {
        return this.getFilteredPersons(predicate).findFirst().orElse(null);
    }

    public void clear(){
        this.persons.clear();
        this.dump();
    }

    public String getCollectionType(){
        return this.persons.getClass().getSimpleName();
    }

    public int getLength(){
        return this.persons.size();
    }

    public ArrayList<Person> getPersons() {
        return new ArrayList<>(this.persons);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Person person : this.persons) {
            sb.append(person).append("\n");
        }
        return sb.toString();
    }
}
