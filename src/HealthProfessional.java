import java.util.Arrays;
import java.util.Objects;

/**
 * Abstract base class for every health professional in the clinic.
 * A generic professional cannot be created; each specialty is a subclass.
 *
 * @author kwu
 * @version 3.0
 */
public abstract class HealthProfessional {

    public static final String[] DEFAULT_WORKING_DAYS =
            {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    public static final String DEFAULT_SHIFT_START = "08:00";
    public static final String DEFAULT_SHIFT_END = "16:00";

    private static final String[] VALID_DAYS =
            {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private int id;
    private String name;
    private String department;
    private String profile;
    private String[] workingDays;
    private String shiftStart;
    private String shiftEnd;

    /**
     * Creates a placeholder professional with safe default values.
     */
    protected HealthProfessional() {
        this(1, "Unassigned", "General Outpatients", "Not specified",
                DEFAULT_WORKING_DAYS, DEFAULT_SHIFT_START, DEFAULT_SHIFT_END);
    }

    /**
     * Creates a professional. Invalid values are rejected at the door,
     * so an object can never exist in an invalid state.
     *
     * @param id          positive numeric staff ID
     * @param name        full name
     * @param department  clinic department
     * @param profile     short biography and expertise
     * @param workingDays fixed set of weekdays worked
     * @param shiftStart  first bookable slot, "HH:mm"
     * @param shiftEnd    last bookable slot, "HH:mm"
     * @throws IllegalArgumentException if any value is invalid
     */
    protected HealthProfessional(int id, String name, String department, String profile,
                                 String[] workingDays, String shiftStart, String shiftEnd) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number: " + id);
        }
        this.id = id;
        this.name = requireText(name, "Name");
        this.department = requireText(department, "Department");
        this.profile = requireText(profile, "Profile");
        this.workingDays = copyValidDays(workingDays);
        this.shiftStart = requireText(shiftStart, "Shift start");
        this.shiftEnd = requireText(shiftEnd, "Shift end");
        checkShift(this.shiftStart, this.shiftEnd);
    }

    /** @return the specific professional type, e.g. "Surgeon" */
    public abstract String getProfessionalType();

    /** @return a short summary of this professional's clinical role */
    public abstract String getClinicalRole();

    /** @return the numeric staff ID (no setter: the ID identifies the professional) */
    public int getId() {
        return id;
    }

    /** @return the full name */
    public String getName() {
        return name;
    }

    /** @param name the new name */
    public void setName(String name) {
        this.name = requireText(name, "Name");
    }

    /** @return the department */
    public String getDepartment() {
        return department;
    }

    /** @param department the new department */
    public void setDepartment(String department) {
        this.department = requireText(department, "Department");
    }

    /** @return the biography and expertise */
    public String getProfile() {
        return profile;
    }

    /** @param profile the new biography and expertise */
    public void setProfile(String profile) {
        this.profile = requireText(profile, "Profile");
    }

    /** @return a copy of the working days */
    public String[] getWorkingDays() {
        // Return a copy so callers cannot change our internal array
        return copyValidDays(workingDays);
    }

    /** @param workingDays the new working days */
    public void setWorkingDays(String[] workingDays) {
        this.workingDays = copyValidDays(workingDays);
    }

    /** @return the first bookable slot */
    public String getShiftStart() {
        return shiftStart;
    }

    /** @return the last bookable slot */
    public String getShiftEnd() {
        return shiftEnd;
    }

    /**
     * Changes the daily shift.
     *
     * @param shiftStart first bookable slot, "HH:mm"
     * @param shiftEnd   last bookable slot, "HH:mm"
     */
    public void setShift(String shiftStart, String shiftEnd) {
        String start = requireText(shiftStart, "Shift start");
        String end = requireText(shiftEnd, "Shift end");
        checkShift(start, end);
        this.shiftStart = start;
        this.shiftEnd = end;
    }

    /**
     * Checks whether a time slot falls inside this professional's shift.
     *
     * @param timeSlot slot in "HH:mm" format
     * @return true if the slot is within the shift
     */
    public boolean isWithinShift(String timeSlot) {
        // Zero-padded "HH:mm" strings compare in the same order as the times they represent
        return timeSlot.compareTo(shiftStart) >= 0 && timeSlot.compareTo(shiftEnd) <= 0;
    }

    /** Prints every field of this professional, one per line. */
    public void displayDoctorInfo() {
        System.out.print(this);
    }

    /**
     * Rejects null or blank text.
     *
     * @param value     the text to check
     * @param fieldName name used in the error message
     * @return the trimmed text
     * @throws IllegalArgumentException if the text is null or blank
     */
    protected static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    private static void checkShift(String start, String end) {
        if (!Appointment.isValidTimeFormat(start) || !Appointment.isValidTimeFormat(end)
                || start.compareTo(end) >= 0) {
            throw new IllegalArgumentException("Invalid shift: " + start + " - " + end);
        }
    }

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

    private static String matchDay(String day) {
        for (String valid : VALID_DAYS) {
            if (valid.equalsIgnoreCase(requireText(day, "Working day"))) {
                return valid;
            }
        }
        throw new IllegalArgumentException("Unknown working day: " + day);
    }

    @Override
    public String toString() {
        return Display.field("Doctor ID", id)
                + Display.field("Name", name)
                + Display.field("Professional Type", getProfessionalType())
                + Display.field("Department", department)
                + Display.field("Profile / Expertise", profile)
                + Display.field("Clinical Role", getClinicalRole())
                + Display.field("Working Days", Arrays.toString(workingDays))
                + Display.field("Shift", shiftStart + " - " + shiftEnd);
    }

    /** Two professionals are the same person if they share the same staff ID. */
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

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
