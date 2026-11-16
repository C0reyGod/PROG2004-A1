/*
 * File: Patient.java
 * Models a clinic patient with input validation and linked appointments.
 */
import java.util.ArrayList;
import java.util.Objects;

/**
 * A clinic patient with validated details and a list of current bookings.
 */
public class Patient {

    public static final int MIN_AGE = 1;
    public static final int MAX_AGE = 120;
    public static final String DEFAULT_GENDER = "Not specified";

    private static final String[] VALID_GENDERS = {"Male", "Female", "Other", DEFAULT_GENDER};

    private String patientId;
    private String name;
    private String gender;
    private int age;
    private String mobilePhone;
    private String conditionDescription;
    private ArrayList<Appointment> appointments = new ArrayList<>();

    // Creates a placeholder patient with safe minimum defaults
    public Patient() {
        this("UNKNOWN", "Unknown Patient", DEFAULT_GENDER, MIN_AGE, "00000000", "Not specified");
    }

    /*
     * Creates a validated patient record after checking ID, name, gender,
     * age range, mobile number digits and symptoms text.
     */
    public Patient(String patientId, String name, String gender, int age,
                   String mobilePhone, String conditionDescription) {
        this.patientId = requireText(patientId, "Patient ID");
        this.name = requireText(name, "Patient name");
        this.gender = requireGender(gender);
        this.age = requireAge(age);
        this.mobilePhone = requirePhone(mobilePhone);
        this.conditionDescription = requireText(conditionDescription, "Condition description");
    }

    // Checks whether an age falls within the accepted 1-120 range
    public static boolean isValidAge(int age) {
        return age >= MIN_AGE && age <= MAX_AGE;
    }

    // Checks whether a gender matches one of the accepted clinic options
    public static boolean isValidGender(String gender) {
        return matchGender(gender) != null;
    }

    /*
     * Checks that a mobile number contains 8 to 15 numeric digits. Stored as
     * a String rather than int so leading zeros (such as "04...") stay intact.
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().length() < 8 || phone.trim().length() > 15) {
            return false;
        }
        String digits = phone.trim();
        for (int i = 0; i < digits.length(); i++) {
            if (digits.charAt(i) < '0' || digits.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    // Rejects null or blank text and returns the trimmed string
    private static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    // Validates and returns a canonical gender label shared by patients and staff
    public static String requireGender(String gender) {
        String matched = matchGender(gender);
        if (matched == null) {
            throw new IllegalArgumentException(
                    "Gender must be Male, Female, Other or Not specified: " + gender);
        }
        return matched;
    }

    // Matches gender case-insensitively against VALID_GENDERS or returns null
    private static String matchGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            return null;
        }
        String cleaned = gender.trim();
        for (String valid : VALID_GENDERS) {
            if (valid.equalsIgnoreCase(cleaned)) {
                return valid;
            }
        }
        return null;
    }

    // Validates and returns age, throwing if outside 1-120
    private static int requireAge(int age) {
        if (!isValidAge(age)) {
            throw new IllegalArgumentException("Age must be " + MIN_AGE + " to " + MAX_AGE + ": " + age);
        }
        return age;
    }

    // Validates and returns a trimmed 8-15 digit phone number
    private static String requirePhone(String phone) {
        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException("Mobile phone must be 8-15 digits: " + phone);
        }
        return phone.trim();
    }

    // Returns the ID card or patient number
    public String getPatientId() {
        return patientId;
    }

    // Updates the ID card or patient number
    public void setPatientId(String patientId) {
        this.patientId = requireText(patientId, "Patient ID");
    }

    // Returns the patient's full name
    public String getName() {
        return name;
    }

    // Updates the patient's full name
    public void setName(String name) {
        this.name = requireText(name, "Patient name");
    }

    // Returns the patient's gender
    public String getGender() {
        return gender;
    }

    // Updates the patient's gender after validation
    public void setGender(String gender) {
        this.gender = requireGender(gender);
    }

    // Returns the patient's age in years
    public int getAge() {
        return age;
    }

    // Updates the patient's age after range validation
    public void setAge(int age) {
        this.age = requireAge(age);
    }

    // Returns the mobile phone number
    public String getMobilePhone() {
        return mobilePhone;
    }

    // Updates the mobile phone number after digit validation
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = requirePhone(mobilePhone);
    }

    // Returns the patient's symptom or visit description
    public String getConditionDescription() {
        return conditionDescription;
    }

    // Updates the symptom or visit description
    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = requireText(conditionDescription, "Condition description");
    }

    // Returns a defensive copy of this patient's booked appointments
    public ArrayList<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    // Links a newly created appointment to this patient
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    // Unlinks a cancelled appointment from this patient
    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    // Returns true if the patient currently has at least one booking
    public boolean hasAppointments() {
        return !appointments.isEmpty();
    }

    // Prints all patient fields framed by single separator lines
    public void displayPatientInfo() {
        System.out.println(Display.LINE);
        System.out.print(this);
        System.out.println(Display.LINE);
    }

    // Formats all patient attributes into aligned key-value rows
    @Override
    public String toString() {
        return Display.field("Patient ID / ID Card", patientId)
                + Display.field("Patient Name", name)
                + Display.field("Gender", gender)
                + Display.field("Age", age)
                + Display.field("Mobile Phone", mobilePhone)
                + Display.field("Condition / Symptoms", conditionDescription)
                + Display.field("Booked Appointments", appointments.size());
    }

    // Patients are matched by mobile number so returning patients retain existing bookings
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return mobilePhone.equals(((Patient) o).mobilePhone);
    }

    // Hashes on mobilePhone to stay consistent with equals()
    @Override
    public int hashCode() {
        return Objects.hash(mobilePhone);
    }
}
