package AccessingData;

/**
 * Created by dlo on 26/06/17.
 */
public class Person {
    private final String name;
    private final String email;
    private final String country;
    private final double salary;
    private final int experience;

    public Person(String name, String email, String country, double salary, int experience) {
        this.name = name;
        this.email = email;
        this.country = country;
        this.salary = salary;
        this.experience = experience;
    }

    public String getName() {
        return  name;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return  country;
    }

    public double getSalary() {
        return salary;
    }

    public int getExperience() {
        return experience;
    }
}
