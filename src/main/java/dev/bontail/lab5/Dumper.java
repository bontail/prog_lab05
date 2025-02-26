package main.java.dev.bontail.lab5;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import main.java.dev.bontail.lab5.data.Person;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


/**
 * Class for working with backup and dump
 */
public class Dumper {
    final private static String backupFilename = ".backup.csv";

    public static boolean isActiveBackup(ArrayList<Person> currentPersonsArray){
        ArrayList<Person> dumpPersonsArray = Dumper.read(backupFilename);
        assert dumpPersonsArray != null;
        return !(dumpPersonsArray.containsAll(currentPersonsArray) && currentPersonsArray.containsAll(dumpPersonsArray));
    }

    public static void saveBackup(ArrayList<Person> persons){
        Dumper.write(backupFilename, persons);
    }

    public static void saveDump(ArrayList<Person> persons) {
        String fileName = System.getenv("COLLECTION_FILENAME");
        if (fileName == null) {
            System.out.println("No environment variable COLLECTION_FILENAME found");
            return;
        }
        Dumper.write(fileName, persons);
    }

    public static ArrayList<Person> getBackupPersons(){
        return Dumper.read(backupFilename);
    }

    public static ArrayList<Person> getDumpPersons(){
        String fileName = System.getenv("COLLECTION_FILENAME");
        if (fileName == null) {
            System.out.println("No environment variable COLLECTION_FILENAME found");
            System.exit(1);
            return null;
        }

        return Dumper.read(fileName);
    }

    private static ArrayList<Person> read(String fileName) {
        try{
            File f = new File(fileName);
            if(!f.exists()) {
                System.out.println("File " + fileName + " does not exist");
                System.exit(1);
            }

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

    private static void write(String fileName, ArrayList<Person> persons) {
        try {
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = mapper.schemaFor(Person.class)
                    .withColumnSeparator(';')
                    .withHeader();
            ObjectWriter writer = mapper.writer(schema);
            writer.writeValue(new FileWriter(fileName, StandardCharsets.UTF_8), persons);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
