/*
 * File: Appointment.java
 * Models an immutable same-day booking linking a patient and a clinician.
 */
import java.util.Objects;

/**
 * One appointment linking a patient, a health professional and a time slot.
 */
public class Appointment implements Comparable<Appointment> {

    public static final String TODAY = "Today";

    private static int nextNumber = 1001;

    /*
     * No no-argument constructor or setters: an appointment must stay complete,
     * and editing doctor or time after booking would bypass double-booking checks.
     */
    private String appointmentId;
    private Patient patient;
    private HealthProfessional healthProfessional;
    private String appointmentDate;
    private String timeSlot;

    /*
     * Creates a complete appointment after verifying ID, linked patient and
     * health professional objects, date string and "HH:mm" time format.
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

    // Auto-generates a sequential booking ID for today's appointments
    public Appointment(Patient patient, HealthProfessional healthProfessional, String timeSlot) {
        this("APT-" + nextNumber++, patient, healthProfessional, TODAY, timeSlot);
    }

    /*
     * Validates strict 24-hour "HH:mm" formatting: exact length 5, colon at
     * index 2, hour 00-23 and minute 00-59.
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

    // Returns the unique booking ID
    public String getAppointmentId() {
        return appointmentId;
    }

    // Returns the booked Patient
    public Patient getPatient() {
        return patient;
    }

    // Returns the assigned HealthProfessional
    public HealthProfessional getHealthProfessional() {
        return healthProfessional;
    }

    // Returns the booking date string
    public String getAppointmentDate() {
        return appointmentDate;
    }

    // Returns the scheduled "HH:mm" slot
    public String getTimeSlot() {
        return timeSlot;
    }

    // Prints full appointment, patient and clinician details to the console
    public void displayAppointmentInfo() {
        System.out.print(this);
    }

    // "HH:mm" is zero-padded, so alphabetical order matches chronological order
    @Override
    public int compareTo(Appointment other) {
        return timeSlot.compareTo(other.timeSlot);
    }

    // Combines appointment header fields with composed patient and clinician info
    @Override
    public String toString() {
        return Display.field("Appointment ID", appointmentId)
                + Display.field("Date", appointmentDate)
                + Display.field("Time Slot", timeSlot)
                + "  [Patient]\n" + patient
                + "  [Health Professional]\n" + healthProfessional;
    }

    // Two appointments clash if they book the same professional at the same slot
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

    // Hashes on timeSlot and healthProfessional to match equals()
    @Override
    public int hashCode() {
        return Objects.hash(timeSlot, healthProfessional);
    }
}
