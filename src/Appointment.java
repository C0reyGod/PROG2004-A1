import java.util.Objects;

/**
 * One appointment today: a patient, a health professional and a time slot.
 * <p>
 * There is deliberately no no-argument constructor and no setters: an
 * appointment must never exist in an incomplete state, and changing the
 * professional or time after booking would bypass the double-booking check.
 * To reschedule, cancel and book again through {@link AppointmentManager}.
 * </p>
 *
 * @author kwu
 * @version 3.0
 */
public class Appointment implements Comparable<Appointment> {

    public static final String TODAY = "Today";

    private static int nextNumber = 1001;

    private final String appointmentId;
    private final Patient patient;
    private final HealthProfessional healthProfessional;
    private final String appointmentDate;
    private final String timeSlot;

    /**
     * Creates an appointment with every detail specified.
     *
     * @param appointmentId      unique ID, e.g. "APT-1001"
     * @param patient            the patient
     * @param healthProfessional any kind of health professional
     * @param appointmentDate    the date
     * @param timeSlot           the time, "HH:mm"
     * @throws IllegalArgumentException if any detail is missing or invalid
     */
    public Appointment(String appointmentId, Patient patient, HealthProfessional healthProfessional,
                       String appointmentDate, String timeSlot) {
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Appointment ID cannot be empty.");
        }
        if (patient == null || healthProfessional == null) {
            throw new IllegalArgumentException("An appointment needs a patient and a health professional.");
        }
        if (appointmentDate == null || appointmentDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Appointment date cannot be empty.");
        }
        if (!isValidTimeFormat(timeSlot)) {
            throw new IllegalArgumentException("Time slot must be HH:mm: " + timeSlot);
        }
        this.appointmentId = appointmentId.trim();
        this.patient = patient;
        this.healthProfessional = healthProfessional;
        this.appointmentDate = appointmentDate.trim();
        this.timeSlot = timeSlot;
    }

    /**
     * Creates an appointment for today with the next available ID.
     *
     * @param patient            the patient
     * @param healthProfessional any kind of health professional
     * @param timeSlot           the time, "HH:mm"
     */
    public Appointment(Patient patient, HealthProfessional healthProfessional, String timeSlot) {
        this("APT-" + nextNumber++, patient, healthProfessional, TODAY, timeSlot);
    }

    /**
     * Checks that a time is written as a valid 24-hour "HH:mm".
     *
     * @param time the text to check
     * @return true if valid
     */
    public static boolean isValidTimeFormat(String time) {
        if (time == null || time.length() != 5 || time.charAt(2) != ':') {
            return false;
        }
        try {
            int hours = Integer.parseInt(time.substring(0, 2));
            int minutes = Integer.parseInt(time.substring(3));
            return hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** @return the appointment ID */
    public String getAppointmentId() {
        return appointmentId;
    }

    /** @return the patient */
    public Patient getPatient() {
        return patient;
    }

    /** @return the health professional */
    public HealthProfessional getHealthProfessional() {
        return healthProfessional;
    }

    /** @return the date */
    public String getAppointmentDate() {
        return appointmentDate;
    }

    /** @return the time slot */
    public String getTimeSlot() {
        return timeSlot;
    }

    /** Prints every field of this appointment, one per line. */
    public void displayAppointmentInfo() {
        System.out.print(this);
    }

    /** Orders appointments from earliest to latest time. */
    @Override
    public int compareTo(Appointment other) {
        // "HH:mm" is zero-padded, so alphabetical order is also time order
        return timeSlot.compareTo(other.timeSlot);
    }

    @Override
    public String toString() {
        return Display.field("Appointment ID", appointmentId)
                + Display.field("Date", appointmentDate)
                + Display.field("Time Slot", timeSlot)
                + "  [Patient]\n" + patient
                + "  [Health Professional]\n" + healthProfessional;
    }

    /** Two appointments clash if they book the same professional at the same time. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appointment)) {
            return false;
        }
        Appointment other = (Appointment) o;
        return timeSlot.equals(other.timeSlot) && healthProfessional.equals(other.healthProfessional);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeSlot, healthProfessional);
    }
}
