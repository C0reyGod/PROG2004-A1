/*
 * File: AppointmentManager.java
 * Central clinic coordinator for staff rosters, booking rules and orders.
 */
import java.util.ArrayList;
import java.util.Collections;

/**
 * Manages clinic staff, daily time slots, bookings, filtering, sorting,
 * cancellation and capability-based clinical orders.
 */
public class AppointmentManager {

    public static final int DEFAULT_OPEN_HOUR = 8;
    public static final int DEFAULT_CLOSE_HOUR = 16;
    public static final int DEFAULT_SLOT_INTERVAL = 30;

    private String clinicName;
    private String[] timeSlots;
    private ArrayList<HealthProfessional> staff = new ArrayList<>();
    private ArrayList<Appointment> appointments = new ArrayList<>();

    /*
     * Creates a clinic manager with custom opening hours and slot interval
     * after validating non-blank clinicName.
     */
    public AppointmentManager(String clinicName, int openHour, int closeHour, int intervalMinutes) {
        if (clinicName == null || clinicName.trim().isEmpty()) {
            throw new IllegalArgumentException("Clinic name cannot be empty.");
        }
        this.clinicName = clinicName.trim();
        this.timeSlots = generateTimeSlots(openHour, closeHour, intervalMinutes);
    }

    // Creates a clinic open 08:00 to 16:00 using standard 30-minute slots
    public AppointmentManager(String clinicName) {
        this(clinicName, DEFAULT_OPEN_HOUR, DEFAULT_CLOSE_HOUR, DEFAULT_SLOT_INTERVAL);
    }

    /*
     * Generates the fixed clinic schedule from open/close/interval parameters
     * so changing clinic hours requires no changes to booking validation logic.
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

    // Returns the clinic name
    public String getClinicName() {
        return clinicName;
    }

    // Returns a defensive copy of the clinic's daily time slots
    public String[] getTimeSlots() {
        String[] copy = new String[timeSlots.length];
        for (int i = 0; i < timeSlots.length; i++) {
            copy[i] = timeSlots[i];
        }
        return copy;
    }

    // Returns the current number of active appointments
    public int getAppointmentCount() {
        return appointments.size();
    }

    // Returns a defensive shallow copy of the registered staff roster
    public ArrayList<HealthProfessional> getStaff() {
        return new ArrayList<>(staff);
    }

    /*
     * Adds a health professional to the clinic roster if non-null and not
     * already present by staff ID.
     */
    public boolean registerProfessional(HealthProfessional professional) {
        if (professional == null || staff.contains(professional)) {
            System.out.println("[WARNING] Professional is missing or already registered.");
            return false;
        }
        staff.add(professional);
        return true;
    }

    // Searches the staff roster by numeric staff ID
    public HealthProfessional findProfessionalById(int id) {
        for (HealthProfessional professional : staff) {
            if (professional.getId() == id) {
                return professional;
            }
        }
        return null;
    }

    // Reuses an existing Patient record matched by mobile number so prior bookings stay linked
    public Patient findPatientByPhone(String phone) {
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getMobilePhone().equals(phone)) {
                return appointment.getPatient();
            }
        }
        return null;
    }

    // Checks whether a slot matches any entry in the clinic's daily schedule
    public boolean isClinicTimeSlot(String timeSlot) {
        for (String slot : timeSlots) {
            if (slot.equals(timeSlot)) {
                return true;
            }
        }
        return false;
    }

    // Returns true if the given professional already holds a booking at timeSlot
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

    // Collects all daily clinic slots that are inside the clinician's shift and unbooked
    public ArrayList<String> getFreeSlots(HealthProfessional professional) {
        ArrayList<String> free = new ArrayList<>();
        for (String slot : timeSlots) {
            if (professional.isWithinShift(slot) && !isBooked(professional, slot)) {
                free.add(slot);
            }
        }
        return free;
    }

    // Prints every registered professional along with today's free booking slots
    public void displayDoctorList() {
        Display.header(clinicName + " - Health Professionals (" + staff.size() + ")");
        for (HealthProfessional professional : staff) {
            System.out.print(professional);
            System.out.print(Display.field("Free Slots Today", getFreeSlots(professional)));
            System.out.println(Display.LINE);
        }
    }

    /*
     * Validates non-null references, clinic schedule membership, clinician
     * shift hours and double-booking before adding an appointment.
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

    // Filters active appointments by clinician staff ID
    public ArrayList<Appointment> filterByProfessionalId(int professionalId) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getHealthProfessional().getId() == professionalId) {
                result.add(appointment);
            }
        }
        return result;
    }

    // Filters active appointments by patient mobile phone number
    public ArrayList<Appointment> filterByPatientPhone(String phone) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getMobilePhone().equals(phone)) {
                result.add(appointment);
            }
        }
        return result;
    }

    // Returns a chronologically sorted copy using Appointment.compareTo
    public ArrayList<Appointment> sortByTime(ArrayList<Appointment> list) {
        ArrayList<Appointment> sorted = new ArrayList<>(list);
        Collections.sort(sorted);
        return sorted;
    }

    // Returns a copy of all active appointments in insertion order
    public ArrayList<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    // Prints a formatted list of appointments or a warning when none match
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

    // Displays only the logged-in patient's appointments sorted chronologically
    public void displayMyAppointments(Patient patient) {
        printAppointments("My appointments - " + patient.getName(),
                sortByTime(filterByPatientPhone(patient.getMobilePhone())));
    }

    /*
     * Cancels one booking belonging exclusively to the given patient, matching
     * by appointment ID or time slot to enforce patient data isolation.
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

    /*
     * Cancels the appointment matching both staff ID and time slot from the
     * clinic management view.
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

    /*
     * Cancels every appointment matching a patient mobile number using reverse
     * iteration so removals do not skip adjacent items.
     */
    public boolean cancelAppointment(String phone) {
        boolean cancelled = false;
        // Walk backwards so removing an item does not skip the next element
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

    // Removes an appointment at index, unlinks it from the patient, and logs confirmation
    private void removeAt(int index) {
        Appointment removed = appointments.remove(index);
        removed.getPatient().removeAppointment(removed);
        System.out.println("[SUCCESS] Cancelled " + removed.getAppointmentId() + " at "
                + removed.getTimeSlot() + ": " + removed.getPatient().getName()
                + " with " + removed.getHealthProfessional().getName() + ".");
    }

    /*
     * Records a medication prescription against any Prescribable professional
     * and appends restricted-schedule status, supply days and default repeats.
     */
    public String recordPrescription(Prescribable prescriber, String patientName, String medication) {
        if (prescriber == null) {
            throw new IllegalArgumentException("Prescriber cannot be null.");
        }
        return prescriber.prescribe(patientName, medication)
                + String.format(" [Restricted-schedule eligible: %s | Max supply: %d days | Default repeats: %d]",
                        prescriber.canPrescribeRestrictedMedication() ? "Yes" : "No",
                        prescriber.getMaxSupplyDays(),
                        Prescribable.DEFAULT_REPEAT_COUNT);
    }

    /*
     * Records a diagnostic order against any DiagnosticCapable professional and
     * appends equipment details, priority label and estimated turnaround time.
     */
    public String recordDiagnosticOrder(DiagnosticCapable diagnostician, String patientName, boolean isUrgent) {
        if (diagnostician == null) {
            throw new IllegalArgumentException("Diagnostician cannot be null.");
        }
        return diagnostician.performDiagnostic(patientName)
                + String.format(" [Equipment: %s | Priority: %s | Est. turnaround: %d mins]",
                        diagnostician.getDiagnosticEquipment(),
                        isUrgent ? "URGENT" : "ROUTINE",
                        diagnostician.estimateTurnaroundMinutes(isUrgent));
    }
}
