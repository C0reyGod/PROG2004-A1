/*
 * File: HealthProfessional.java
 * Defines the abstract base class shared by every clinical specialty.
 */
import java.util.Objects;

/**
 * Abstract base class for every health professional in the clinic.
 * Cannot be instantiated directly; each specialty extends this class.
 */
public abstract class HealthProfessional {

    public static final String[] DEFAULT_WORKING_DAYS =
            {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    public static final String DEFAULT_SHIFT_START = "08:00";
    public static final String DEFAULT_SHIFT_END = "16:00";
    public static final String DEFAULT_GENDER = Patient.DEFAULT_GENDER;

    private static final String[] VALID_DAYS =
            {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private int id;
    private String name;
    private String gender;
    private String department;
    private String profile;
    private String[] workingDays;
    private String shiftStart;
    private String shiftEnd;

    // Creates a placeholder professional using safe default values
    protected HealthProfessional() {
        this(1, "Unassigned", DEFAULT_GENDER, "General Outpatients", "Not specified",
                DEFAULT_WORKING_DAYS, DEFAULT_SHIFT_START, DEFAULT_SHIFT_END);
    }

    /*
     * Validates every field on creation so an invalid HealthProfessional
     * object can never exist in memory.
     */
    protected HealthProfessional(int id, String name, String gender, String department, String profile,
                                 String[] workingDays, String shiftStart, String shiftEnd) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number: " + id);
        }
        this.id = id;
        this.name = requireText(name, "Name");
        this.gender = Patient.requireGender(gender);
        this.department = requireText(department, "Department");
        this.profile = requireText(profile, "Profile");
        this.workingDays = copyValidDays(workingDays);
        this.shiftStart = requireText(shiftStart, "Shift start");
        this.shiftEnd = requireText(shiftEnd, "Shift end");
        checkShift(this.shiftStart, this.shiftEnd);
    }

    // Returns the specific specialty title implemented by each subclass
    public abstract String getProfessionalType();

    // Returns a short clinical role summary implemented by each subclass
    public abstract String getClinicalRole();

    // Returns the staff ID (no setter because ID uniquely identifies the person)
    public int getId() {
        return id;
    }

    // Returns the full name
    public String getName() {
        return name;
    }

    // Updates the full name after validating non-blank text
    public void setName(String name) {
        this.name = requireText(name, "Name");
    }

    // Returns the clinical professional's gender
    public String getGender() {
        return gender;
    }

    // Updates the clinical professional's gender after validation
    public void setGender(String gender) {
        this.gender = Patient.requireGender(gender);
    }

    // Returns the clinic department
    public String getDepartment() {
        return department;
    }

    // Updates the department after validating non-blank text
    public void setDepartment(String department) {
        this.department = requireText(department, "Department");
    }

    // Returns the biography and expertise summary
    public String getProfile() {
        return profile;
    }

    // Updates the biography and expertise summary
    public void setProfile(String profile) {
        this.profile = requireText(profile, "Profile");
    }

    // Returns a defensive copy so callers cannot modify the internal array
    public String[] getWorkingDays() {
        return copyValidDays(workingDays);
    }

    // Replaces working days after validating every weekday name
    public void setWorkingDays(String[] workingDays) {
        this.workingDays = copyValidDays(workingDays);
    }

    // Returns the first bookable shift slot ("HH:mm")
    public String getShiftStart() {
        return shiftStart;
    }

    // Returns the last bookable shift slot ("HH:mm")
    public String getShiftEnd() {
        return shiftEnd;
    }

    /*
     * Updates both shift boundaries together so the start-before-end
     * constraint is validated atomically.
     */
    public void setShift(String shiftStart, String shiftEnd) {
        String start = requireText(shiftStart, "Shift start");
        String end = requireText(shiftEnd, "Shift end");
        checkShift(start, end);
        this.shiftStart = start;
        this.shiftEnd = end;
    }

    /*
     * Checks that timeSlot is a valid 24-hour "HH:mm" string and falls
     * inclusively within [shiftStart, shiftEnd].
     */
    public boolean isWithinShift(String timeSlot) {
        if (!Appointment.isValidTimeFormat(timeSlot)) {
            return false;
        }
        // Zero-padded "HH:mm" strings compare in the same order as the times they represent
        return timeSlot.compareTo(shiftStart) >= 0 && timeSlot.compareTo(shiftEnd) <= 0;
    }

    // Prints all fields of this professional to the console
    public void displayDoctorInfo() {
        System.out.print(this);
    }

    // Rejects null or whitespace-only text and returns the trimmed value
    protected static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    /*
     * Ensures both shift endpoints follow "HH:mm" format and that shiftStart
     * comes strictly before shiftEnd.
     */
    private static void checkShift(String start, String end) {
        if (!Appointment.isValidTimeFormat(start) || !Appointment.isValidTimeFormat(end)
                || start.compareTo(end) >= 0) {
            throw new IllegalArgumentException("Invalid shift: " + start + " - " + end);
        }
    }

    /*
     * Checks that the weekday array is non-empty and returns a freshly
     * allocated copy with canonical weekday capitalisation.
     */
    private static String[] copyValidDays(String[] days) {
        if (days == null || days.length == 0) {
            throw new IllegalArgumentException("At least one working day is required.");
        }
        String[] copy = new String[days.length];
        for (int i = 0; i < days.length; i++) {
            copy[i] = matchDay(days[i]);
        }
        return copy;
    }

    // Matches a weekday string case-insensitively against VALID_DAYS
    private static String matchDay(String day) {
        for (String valid : VALID_DAYS) {
            if (valid.equalsIgnoreCase(requireText(day, "Working day"))) {
                return valid;
            }
        }
        throw new IllegalArgumentException("Unknown working day: " + day);
    }

    // Formats all shared clinician fields into aligned key-value rows
    @Override
    public String toString() {
        return Display.field("Doctor ID", id)
                + Display.field("Name", name)
                + Display.field("Gender", gender)
                + Display.field("Professional Type", getProfessionalType())
                + Display.field("Department", department)
                + Display.field("Profile / Expertise", profile)
                + Display.field("Clinical Role", getClinicalRole())
                + Display.field("Working Days", Display.formatStringArray(workingDays))
                + Display.field("Shift", shiftStart + " - " + shiftEnd);
    }

    // Two professionals represent the same person if they share the same staff ID
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HealthProfessional)) {
            return false;
        }
        return id == ((HealthProfessional) o).id;
    }

    // Hashes on staff ID to remain consistent with equals()
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
