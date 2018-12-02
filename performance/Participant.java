package performance;

import java.util.Random;

public class Participant {

    private String name;
    private String surname;
    private String partNo ;
    private String projCode = "ES";
    private static int No = 0;
    private String country;
    private int age;
    private String gender;
    private boolean came;

    public Participant(){}

    public Participant(String name, String surname, String country, int age, String gender){
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.age = age;
        this.gender = gender;
        this.partNo = projCode + (No++);
    }

    private Participant(Builder aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCountry() {
        return country;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public boolean isCame() {
        return came;
    }

    public void setCame(boolean came) {
        this.came = came;
    }

    public String getPartNo() {
        return partNo;
    }

    @Override
    public String toString() {
        return String.format("%-21s%-21s %-8s %-3s %-3s %-6s", name, surname, country, age, gender, came);
    }

    public static class Builder {

        private final static Random RANDOM = new Random(1949);
        private final static String[] NAMES =
                {"Kazys", "Sara", "Joana", "Doantas", "Paulius","Neapolas"};
        private final static String[] SURNAMES =
                {"Pavardenis", "Surneimis", "Pavardziukas", "GalPravardziukas"};
        private final static String[] COUNTRIES =
                {"Lietuva", "Lenkija", "Ispanijam", "Ukraina", "Kipras"};
        private final static String[] GENDER =
                {"m", "v"};

        private String name = "";
        private String surname = "";
        private String country = "";
        private String gender = "";
        private int age = -1;
        private boolean came = false;

        public Participant build() {
            return new Participant(this);
        }

        public Participant buildRandom() {
            int ge = RANDOM.nextInt(GENDER.length);
            return new Participant(
                    NAMES[RANDOM.nextInt(NAMES.length)],
                    SURNAMES[RANDOM.nextInt(SURNAMES.length)],
                    COUNTRIES[RANDOM.nextInt(COUNTRIES.length)],
                    1980 + RANDOM.nextInt(20),
                    GENDER[ge]);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder rida(int age) {
            this.age = age;
            return this;
        }

        public Builder kaina(boolean came) {
            this.came = came;
            return this;
        }
    }
}
