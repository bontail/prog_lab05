package main.java.dev.bontail.lab5;
import main.java.dev.bontail.lab5.data.Person;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;


/**
 * Class for save Person collection
 * All actions go through this class
 */
public class PersonManager {
    final private ArrayDeque<Person> persons = new ArrayDeque<>();
    final public java.util.Date creationDate;

    public PersonManager() {
        this.creationDate = new java.util.Date();
        this.persons.addAll(Dumper.getDumpPersons());
    }

    private void backup(){
        Dumper.saveBackup(new ArrayList<>(this.persons));
    }

    public void dump(){
        Dumper.saveDump(new ArrayList<>(this.persons));
    }

    public void addPerson(Person person) {
        this.persons.addLast(person);
        Person[] personsArray = this.persons.toArray(new Person[0]);
        Arrays.sort(personsArray);
        this.persons.clear();
        this.persons.addAll(Arrays.asList(personsArray));
        this.backup();
    }

    public void removePerson(Person person) {
        this.persons.removeFirstOccurrence(person);
        this.backup();
    }

    public Stream<Person> getFilteredPersons(Predicate<Person> predicate) {
        return this.persons.stream().filter(predicate);
    }

    public Person getPersonByField(Predicate<Person> predicate) {
        return this.getFilteredPersons(predicate).findFirst().orElse(null);
    }

    public void clear(){
        this.persons.clear();
        this.backup();
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

    public void setPersons(ArrayList<Person> persons) {
        this.persons.clear();
        this.persons.addAll(persons);
        this.backup();
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
