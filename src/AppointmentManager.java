import java.util.ArrayList;
import java.util.Collections;

/**
 * Keeps the clinic's staff and all of today's appointments in one place.
 * Patients can only see and cancel their own bookings; the clinic view can
 * filter, sort and cancel any booking.
 *
 * @author kwu
 * @version 3.0
 */
public class AppointmentManager {

    private String clinicName;
    private String[] timeSlots;
    private ArrayList<HealthProfessional> staff = new ArrayList<>();
    private ArrayList<Appointment> appointments = new ArrayList<>();

    /**
     * Creates a clinic with custom opening hours.
     *
     * @param clinicName      the clinic's name
     * @param openHour        first slot hour, e.g. 8
     * @param closeHour       last slot hour, e.g. 16
     * @param intervalMinutes minutes between slots, e.g. 30
     */
    public AppointmentManager(String clinicName, int openHour, int closeHour, int intervalMinutes) {
        if (clinicName == null || clinicName.trim().isEmpty()) {
            throw new IllegalArgumentException("Clinic name cannot be empty.");
        }
        this.clinicName = clinicName.trim();
        this.timeSlots = generateTimeSlots(openHour, closeHour, intervalMinutes);
    }

    /**
     * Creates a clinic open 08:00 to 16:00 in 30-minute slots.
     *
     * @param clinicName the clinic's name
     */
    public AppointmentManager(String clinicName) {
        this(clinicName, 8, 16, 30);
    }

    /**
     * Builds the fixed list of daily slots. The hours are parameters, so the
     * clinic can change its schedule without touching any booking logic.
     *
     * @param openHour        first slot hour
     * @param closeHour       last slot hour
     * @param intervalMinutes minutes between slots
     * @return the slots in "HH:mm" format, earliest first
     */
    public static String[] generateTimeSlots(int openHour, int closeHour, int intervalMinutes) {
        if (openHour < 0 || closeHour > 23 || openHour >= closeHour || intervalMinutes <= 0) {
            throw new IllegalArgumentException("Invalid clinic hours.");
        }
        int count = (closeHour - openHour) * 60 / intervalMinutes + 1;
        String[] slots = new String[count];
        for (int i = 0; i < count; i++) {
            int minutes = openHour * 60 + i * intervalMinutes;
            slots[i] = String.format("%02d:%02d", minutes / 60, minutes % 60);
        }
        return slots;
    }

    /** @return the clinic's name */
    public String getClinicName() {
        return clinicName;
    }

    /** @return a copy of the daily time slots */
    public String[] getTimeSlots() {
        String[] copy = new String[timeSlots.length];
        for (int i = 0; i < timeSlots.length; i++) {
            copy[i] = timeSlots[i];
        }
        return copy;
    }

    /** @return the number of booked appointments */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /** @return a copy of the staff list */
    public ArrayList<HealthProfessional> getStaff() {
        return new ArrayList<>(staff);
    }

    /**
     * Adds a professional to the staff list.
     *
     * @param professional the professional to add
     * @return true if added, false if null or the ID is already used
     */
    public boolean registerProfessional(HealthProfessional professional) {
        if (professional == null || staff.contains(professional)) {
            System.out.println("[WARNING] Professional is missing or already registered.");
            return false;
        }
        staff.add(professional);
        return true;
    }

    /**
     * Finds a professional by staff ID.
     *
     * @param id the staff ID
     * @return the professional, or null if not found
     */
    public HealthProfessional findProfessionalById(int id) {
        for (HealthProfessional professional : staff) {
            if (professional.getId() == id) {
                return professional;
            }
        }
        return null;
    }

    /**
     * Finds a patient who already has a booking, so a returning patient keeps
     * their existing record.
     *
     * @param phone the mobile number
     * @return the existing patient, or null if none
     */
    public Patient findPatientByPhone(String phone) {
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getMobilePhone().equals(phone)) {
                return appointment.getPatient();
            }
        }
        return null;
    }

    /**
     * Checks whether a slot is part of the clinic's daily schedule.
     *
     * @param timeSlot the slot to check
     * @return true if it is one of the clinic's slots
     */
    public boolean isClinicTimeSlot(String timeSlot) {
        for (String slot : timeSlots) {
            if (slot.equals(timeSlot)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a professional already has a booking at a slot.
     *
     * @param professional the professional
     * @param timeSlot     the slot
     * @return true if already booked
     */
    public boolean isBooked(HealthProfessional professional, String timeSlot) {
        for (Appointment appointment : appointments) {
            // equals() compares staff IDs, so two objects for the same person still clash
            if (appointment.getHealthProfessional().equals(professional)
                    && appointment.getTimeSlot().equals(timeSlot)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lists the slots a professional can still be booked for today.
     *
     * @param professional the professional
     * @return free slots inside their shift
     */
    public ArrayList<String> getFreeSlots(HealthProfessional professional) {
        ArrayList<String> free = new ArrayList<>();
        for (String slot : timeSlots) {
            if (professional.isWithinShift(slot) && !isBooked(professional, slot)) {
                free.add(slot);
            }
        }
        return free;
    }

    /** Prints every professional, one field per line, with their free slots. */
    public void displayDoctorList() {
        Display.header(clinicName + " - Health Professionals (" + staff.size() + ")");
        for (HealthProfessional professional : staff) {
            System.out.print(professional);
            System.out.print(Display.field("Free Slots Today", getFreeSlots(professional)));
            System.out.println(Display.LINE);
        }
    }

    /**
     * Books an appointment after checking every rule.
     *
     * @param patient      the patient
     * @param professional any kind of health professional
     * @param timeSlot     the requested slot
     * @return true if booked, false (with a warning) if any rule fails
     */
    public boolean addAppointment(Patient patient, HealthProfessional professional, String timeSlot) {
        String warning = null;
        if (patient == null || professional == null) {
            warning = "a patient and a health professional are both required.";
        } else if (!isClinicTimeSlot(timeSlot)) {
            warning = timeSlot + " is not a clinic slot (" + timeSlots[0] + " to "
                    + timeSlots[timeSlots.length - 1] + ").";
        } else if (!professional.isWithinShift(timeSlot)) {
            warning = professional.getName() + " only works " + professional.getShiftStart()
                    + " - " + professional.getShiftEnd() + ".";
        } else if (isBooked(professional, timeSlot)) {
            warning = professional.getName() + " is already booked at " + timeSlot + ".";
        }
        if (warning != null) {
            System.out.println("[WARNING] Booking rejected: " + warning);
            return false;
        }

        Appointment appointment = new Appointment(patient, professional, timeSlot);
        appointments.add(appointment);
        patient.addAppointment(appointment);
        System.out.println("[SUCCESS] Appointment booked. Details:");
        System.out.print(appointment);
        System.out.println(Display.LINE);
        return true;
    }

    /**
     * Returns the appointments of one professional.
     *
     * @param professionalId the staff ID
     * @return matching appointments, in booking order
     */
    public ArrayList<Appointment> filterByProfessionalId(int professionalId) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getHealthProfessional().getId() == professionalId) {
                result.add(appointment);
            }
        }
        return result;
    }

    /**
     * Returns the appointments of one patient.
     *
     * @param phone the patient's mobile number
     * @return matching appointments, in booking order
     */
    public ArrayList<Appointment> filterByPatientPhone(String phone) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getMobilePhone().equals(phone)) {
                result.add(appointment);
            }
        }
        return result;
    }

    /**
     * Returns a sorted copy, earliest first. Works for all appointments or
     * for any filtered subset.
     *
     * @param list the appointments to sort
     * @return a new sorted list
     */
    public ArrayList<Appointment> sortByTime(ArrayList<Appointment> list) {
        ArrayList<Appointment> sorted = new ArrayList<>(list);
        Collections.sort(sorted);
        return sorted;
    }

    /** @return a copy of all appointments, in booking order */
    public ArrayList<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    /**
     * Prints a list of appointments under a heading.
     *
     * @param title the heading
     * @param list  the appointments to print
     */
    public void printAppointments(String title, ArrayList<Appointment> list) {
        if (list.isEmpty()) {
            System.out.println("[WARNING] No appointments found: " + title + ".");
            return;
        }
        Display.header(title + " (" + list.size() + ")");
        for (Appointment appointment : list) {
            System.out.print(appointment);
            System.out.println(Display.LINE);
        }
    }

    /**
     * Prints only the current patient's appointments, earliest first.
     *
     * @param patient the logged-in patient
     */
    public void displayMyAppointments(Patient patient) {
        printAppointments("My appointments - " + patient.getName(),
                sortByTime(filterByPatientPhone(patient.getMobilePhone())));
    }

    /**
     * Cancels one of the current patient's own appointments.
     *
     * @param patient the logged-in patient
     * @param key     the appointment ID or time slot
     * @return true if cancelled
     */
    public boolean cancelMyAppointment(Patient patient, String key) {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            boolean matches = appointment.getAppointmentId().equalsIgnoreCase(key)
                    || appointment.getTimeSlot().equals(key);
            // Data isolation: a patient may only cancel a booking that is their own
            if (matches && appointment.getPatient().equals(patient)) {
                removeAt(i);
                return true;
            }
        }
        System.out.println("[WARNING] Cancellation failed: you have no appointment matching " + key + ".");
        return false;
    }

    /**
     * Cancels the appointment of a professional at a time (clinic view).
     *
     * @param professionalId the staff ID
     * @param timeSlot       the time slot
     * @return true if cancelled
     */
    public boolean cancelAppointment(int professionalId, String timeSlot) {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getHealthProfessional().getId() == professionalId
                    && appointment.getTimeSlot().equals(timeSlot)) {
                removeAt(i);
                return true;
            }
        }
        System.out.println("[WARNING] Cancellation failed: no appointment for professional "
                + professionalId + " at " + timeSlot + ".");
        return false;
    }

    /**
     * Cancels every appointment of a patient (clinic view).
     *
     * @param phone the patient's mobile number
     * @return true if at least one was cancelled
     */
    public boolean cancelAppointment(String phone) {
        boolean cancelled = false;
        // Walk backwards so removing an item does not skip the next one
        for (int i = appointments.size() - 1; i >= 0; i--) {
            if (appointments.get(i).getPatient().getMobilePhone().equals(phone)) {
                removeAt(i);
                cancelled = true;
            }
        }
        if (!cancelled) {
            System.out.println("[WARNING] Cancellation failed: no appointment for mobile " + phone + ".");
        }
        return cancelled;
    }

    private void removeAt(int index) {
        Appointment removed = appointments.remove(index);
        removed.getPatient().removeAppointment(removed);
        System.out.println("[SUCCESS] Cancelled " + removed.getAppointmentId() + " at "
                + removed.getTimeSlot() + ": " + removed.getPatient().getName()
                + " with " + removed.getHealthProfessional().getName() + ".");
    }
}
