package ru.job4j.inheritance;

/**
 * Patient
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Patient {

    private String name;
    private boolean gender; // true = male; false = female
    private int age;
    private int height;
    private int weight;
    private boolean isSick;
    private String currentDisease;
    private byte diseaseSeverity; // 0 is healthy, 127 is deadly sick
    private String[] medicalHistory;
    private boolean hasMedicalInsurance;
    private long telNumber;

    /**
     * Instance constructor of the Patient class
     *
     * @param name String
     * @param gender booleam, true = male; false = female
     * @param age int
     * @param height int, in cm
     * @param weight int, in kg
     * @param currentDisease String name of current disease, if patient is ill
     * @param hasMedicalInsurance boolean
     */
    public Patient(
            String name,
            boolean gender,
            int age,
            int height,
            int weight,
            String currentDisease,
            boolean hasMedicalInsurance
    ) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.currentDisease = currentDisease;
        this.hasMedicalInsurance = hasMedicalInsurance;
    }

    /**
     * @return String Patient's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return boolean true, if male; false, if female
     */
    public boolean genderIs() {
        return gender;
    }

    /**
     * @return int age
     */
    public int getAge() {
        return age;
    }

    /**
     * @return int height in cm
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return int weight, in kg
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight, int new weight, in kg
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @return boolean
     */
    public boolean isSick() {
        return isSick;
    }

    /**
     * @return String name of current disease, if the patient is ill
     */
    public String getCurrentDisease() {
        return currentDisease;
    }

    /**
     * @param currentDisease String diagnosis from Doctor
     */
    public void setCurrentDisease(String currentDisease) {
        this.currentDisease = currentDisease;
    }

    /**
     * @return byte, numerical expression of the severity of the disease
     * 0 is healthy, 127 is deadly sick
     */
    public byte getDiseaseSeverity() {
        return diseaseSeverity;
    }

    /**
     * @param diseaseSeverity byte, new numerical severity of the disease
     * 0 is healthy, 127 is deadly sick
     */
    void setDiseaseSeverity(byte diseaseSeverity) {
        this.diseaseSeverity = diseaseSeverity;
    }

    /**
     * @return String[] medical history of the Patient
     */
    public String[] getMedicalHistory() {
        return medicalHistory;
    }

    /**
     * @param medicalHistory String[] renewed medical history
     */
    public void setMedicalHistory(String[] medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    /**
     * @return boolean having health insurance
     */
    public boolean isHasMedicalInsurance() {
        return hasMedicalInsurance;
    }

    /**
     * @param hasMedicalInsurance boolean having health insurance
     */
    public void setHasMedicalInsurance(boolean hasMedicalInsurance) {
        this.hasMedicalInsurance = hasMedicalInsurance;
    }

    /**
     *
     * @return long telephone number, without "+" and braces
     */
    public long getTelNumber() {
        return telNumber;
    }

    /**
     * @param telNumber long telephone number, without "+" and braces
     */
    public void setTelNumber(long telNumber) {
        this.telNumber = telNumber;
    }

    /**
     * @return String sound of sneezing
     */
    public String sneeze() {
        return "AHOY!";
    }

    /**
     * Method fallAsleep - sleep helps Patient to recover
     */
    void fallAsleep() {

        if (this.isSick & this.diseaseSeverity > 0) {
            this.diseaseSeverity -= 1;
        } else if (this.age < 20) {
            this.height += 1;
            this.weight += 1;
        }
        if (this.diseaseSeverity <= 0) this.recover();
    }

    /**
     * Method getSick - the patient falls ill
     */
    public void becomeSick() {
        this.isSick = true;
        this.diseaseSeverity = (byte) (Math.random() * 126);
    }

    /**
     * Method recover - the patient is recovering
     */
    void recover() {
        this.isSick = true;
        this.diseaseSeverity = 0;
    }
}
