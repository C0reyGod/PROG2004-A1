/*
 * File: AssignmentOne.java
 * Console entry point running interactive clinic operations and the automated
 * Part 3 and Part 6 demonstration.
 */
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point of the Health Service Appointment System, combining a looping
 * console menu with the scripted Part 3 and Part 6 demonstration.
 */
public class AssignmentOne {

    /*
     * Initializes the clinic roster, loops over the 0-8 main menu and
     * dispatches user selections until option 0 exits.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AppointmentManager clinic = new AppointmentManager("Southern Cross Health Clinic");
        addPreconfiguredDoctors(clinic);
        Patient currentPatient = null;

        int choice = -1;
        while (choice != 0) {
            displayMainMenu(currentPatient, clinic.getAppointmentCount());
            choice = readInt(scanner, "Please enter your choice (0-8): ", 0, 8);
            System.out.println();
            switch (choice) {
                case 1:
                    clinic.displayDoctorList();
                    break;
                case 2:
                    currentPatient = inputPatientInformation(scanner, clinic);
                    break;
                case 3:
                    if (currentPatient == null) {
                        System.out.println("[NOTICE] Please enter your details before booking.");
                        currentPatient = inputPatientInformation(scanner, clinic);
                    }
                    bookAppointment(scanner, clinic, currentPatient);
                    break;
                case 4:
                    if (hasPatient(currentPatient)) {
                        clinic.displayMyAppointments(currentPatient);
                    }
                    break;
                case 5:
                    if (hasPatient(currentPatient)) {
                        cancelMyAppointment(scanner, clinic, currentPatient);
                    }
                    break;
                case 6:
                    viewClinicAppointments(scanner, clinic);
                    break;
                case 7:
                    cancelClinicAppointment(scanner, clinic);
                    break;
                case 8:
                    runDemonstration();
                    break;
                default:
                    System.out.println("Thank you for using the appointment system. Goodbye!");
                    break;
            }
        }
    }

    /*
     * Registers the clinic's fixed roster: three General Practitioners plus one
     * to two specialists across each of the remaining five subclasses.
     */
    public static void addPreconfiguredDoctors(AppointmentManager clinic) {
        clinic.registerProfessional(new GeneralPractitioner(101, "Dr. Sarah Jenkins", "Female",
                "15 years in family medicine and chronic disease care.",
                new String[]{"Monday", "Tuesday", "Wednesday", "Friday"},
                "08:00", "16:00", "GP-101", true));
        clinic.registerProfessional(new GeneralPractitioner(102, "Dr. Michael Chen", "Male",
                "Acute illness and men's health.", HealthProfessional.DEFAULT_WORKING_DAYS,
                "12:00", "16:00", "GP-102", true));
        clinic.registerProfessional(new GeneralPractitioner(103, "Dr. Priya Nair", "Female",
                "Women's and children's health.", new String[]{"Tuesday", "Thursday", "Saturday"},
                "08:00", "12:00", "GP-103", false));
        clinic.registerProfessional(new Surgeon(201, "Dr. Alistair Vance", "Male",
                "Orthopaedic surgeon, joint reconstruction.", new String[]{"Monday", "Wednesday", "Thursday"},
                "08:00", "12:00", "Orthopaedic Surgery", "OT-1"));
        clinic.registerProfessional(new Surgeon(202, "Dr. Elena Rostova", "Female", "Cardiothoracic Surgery"));
        clinic.registerProfessional(new Cardiologist(301, "Dr. Marcus Sterling", "Male",
                "Interventional cardiologist, arrhythmia and stenting.", new String[]{"Monday", "Tuesday", "Thursday"},
                "09:00", "15:00", "Interventional Cardiology", "12-lead ECG and echocardiogram"));
        clinic.registerProfessional(new Radiologist(401, "Dr. Hannah Kim", "Female", "MRI and CT"));
        clinic.registerProfessional(new Nurse(501, "Chloe Evans", "Female", "Outpatient Treatment Ward"));
        clinic.registerProfessional(new Dietitian(601, "Noah Williams", "Male",
                "Diabetes and kidney nutrition.", new String[]{"Tuesday", "Wednesday", "Friday"},
                "09:00", "15:00", "APD-7731", "Diabetes nutrition"));
    }

    // Prints the main menu header, active patient status and booking count
    public static void displayMainMenu(Patient currentPatient, int totalAppointments) {
        System.out.println();
        Display.header("HEALTH SERVICE APPOINTMENT SYSTEM - MAIN MENU");
        String who = currentPatient == null ? "none (choose 2 to enter your details)"
                : currentPatient.getName() + " (" + currentPatient.getMobilePhone() + ")";
        System.out.println("  Current patient : " + who);
        System.out.println("  Total bookings  : " + totalAppointments);
        System.out.println(Display.LINE);
        System.out.println("  1. View all doctors (department, profile, free slots)");
        System.out.println("  2. Enter / switch patient details");
        System.out.println("  3. Book an appointment");
        System.out.println("  4. View my appointments");
        System.out.println("  5. Cancel my appointment");
        System.out.println("  6. [Clinic] View, filter and sort appointments");
        System.out.println("  7. [Clinic] Cancel an appointment");
        System.out.println("  8. Run automated demonstration (Part 3 and Part 6)");
        System.out.println("  0. Exit");
        System.out.println(Display.DOUBLE_LINE);
    }

    /*
     * Reads patient details with validation loops and updates existing patient
     * records matched by phone so prior bookings are preserved.
     */
    public static Patient inputPatientInformation(Scanner scanner, AppointmentManager clinic) {
        Display.header("PATIENT DETAILS");
        String patientId = readText(scanner, "Please enter your ID card number: ");
        String name = readText(scanner, "Please enter your full name: ");
        String gender = readGender(scanner, "Please enter your gender (Male/Female/Other): ");
        int age = readInt(scanner, "Please enter your age (1-120): ", Patient.MIN_AGE, Patient.MAX_AGE);
        String phone = readPhone(scanner, "Please enter your mobile number (8-15 digits): ");
        String condition = readText(scanner, "Please describe your symptoms: ");

        Patient patient = clinic.findPatientByPhone(phone);
        if (patient == null) {
            patient = new Patient(patientId, name, gender, age, phone, condition);
            System.out.println("[SUCCESS] Patient details saved:");
        } else {
            // Same mobile number: update the existing record instead of losing its bookings
            patient.setPatientId(patientId);
            patient.setName(name);
            patient.setGender(gender);
            patient.setAge(age);
            patient.setConditionDescription(condition);
            System.out.println("[SUCCESS] Welcome back. Your details were updated:");
        }
        patient.displayPatientInfo();
        return patient;
    }

    // Prompts for doctor ID and slot, then attempts to book for currentPatient
    public static void bookAppointment(Scanner scanner, AppointmentManager clinic, Patient patient) {
        Display.header("BOOK AN APPOINTMENT FOR " + patient.getName());
        HealthProfessional doctor = readDoctor(scanner, clinic, "Please enter the doctor ID: ");
        System.out.println("Free slots for " + doctor.getName() + ": " + clinic.getFreeSlots(doctor));
        String slot = readTimeSlot(scanner, clinic, "Please enter a time slot (e.g. 09:30): ");
        clinic.addAppointment(patient, doctor, slot);
    }

    // Prompts for booking ID or time slot to cancel one of currentPatient's bookings
    public static void cancelMyAppointment(Scanner scanner, AppointmentManager clinic, Patient patient) {
        if (!patient.hasAppointments()) {
            System.out.println("[WARNING] You have no appointments to cancel.");
            return;
        }
        clinic.displayMyAppointments(patient);
        String key = readText(scanner, "Please enter the appointment ID or time slot to cancel: ");
        clinic.cancelMyAppointment(patient, key);
    }

    /*
     * Runs the clinic-wide appointment viewing menu: unfiltered, chronologically
     * sorted, filtered by clinician ID, or filtered by patient phone.
     */
    public static void viewClinicAppointments(Scanner scanner, AppointmentManager clinic) {
        Display.header("CLINIC VIEW - APPOINTMENTS");
        System.out.println("  1. All appointments (booking order)");
        System.out.println("  2. All appointments sorted by time");
        System.out.println("  3. By doctor ID, sorted by time");
        System.out.println("  4. By patient mobile, sorted by time");
        int option = readInt(scanner, "Please choose an option (1-4): ", 1, 4);
        if (option == 1) {
            clinic.printAppointments("All appointments", clinic.getAllAppointments());
        } else if (option == 2) {
            clinic.printAppointments("All appointments by time",
                    clinic.sortByTime(clinic.getAllAppointments()));
        } else if (option == 3) {
            HealthProfessional doctor = readDoctor(scanner, clinic, "Please enter the doctor ID: ");
            clinic.printAppointments("Appointments with " + doctor.getName(),
                    clinic.sortByTime(clinic.filterByProfessionalId(doctor.getId())));
        } else {
            String phone = readPhone(scanner, "Please enter the patient's mobile number: ");
            clinic.printAppointments("Appointments for " + phone,
                    clinic.sortByTime(clinic.filterByPatientPhone(phone)));
        }
    }

    // Prompts for clinic cancellation either by doctor/slot or by patient phone
    public static void cancelClinicAppointment(Scanner scanner, AppointmentManager clinic) {
        Display.header("CLINIC VIEW - CANCEL");
        System.out.println("  1. By doctor ID and time slot");
        System.out.println("  2. By patient mobile number");
        int option = readInt(scanner, "Please choose an option (1-2): ", 1, 2);
        if (option == 1) {
            HealthProfessional doctor = readDoctor(scanner, clinic, "Please enter the doctor ID: ");
            String slot = readTimeSlot(scanner, clinic, "Please enter the time slot: ");
            clinic.cancelAppointment(doctor.getId(), slot);
        } else {
            clinic.cancelAppointment(readPhone(scanner, "Please enter the patient's mobile number: "));
        }
    }

    // Checks that a patient has logged in via menu option 2 before booking/cancelling
    private static boolean hasPatient(Patient patient) {
        if (patient == null) {
            System.out.println("[WARNING] Please enter your details first (option 2).");
            return false;
        }
        return true;
    }

    // ---------- Input helpers: prompt first, then re-ask until valid ----------

    // Reads a non-empty text line from keyboard input
    private static String readText(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.println("[WARNING] This field cannot be empty.");
            System.out.print(prompt);
            input = scanner.nextLine().trim();
        }
        return input;
    }

    // Reads an integer guaranteed to fall between min and max inclusive
    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // Not a whole number: fall through to range warning
            }
            System.out.println("[WARNING] Please enter a whole number from " + min + " to " + max + ".");
        }
    }

    // Reads a validated gender choice ("Male", "Female", "Other", or "Not specified")
    private static String readGender(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        while (!Patient.isValidGender(input)) {
            System.out.println("[WARNING] Please enter Male, Female, Other, or Not specified.");
            System.out.print(prompt);
            input = scanner.nextLine().trim();
        }
        return Patient.requireGender(input);
    }

    // Reads a valid 8-15 digit mobile phone string
    private static String readPhone(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        while (!Patient.isValidPhone(input)) {
            System.out.println("[WARNING] A mobile number has 8 to 15 digits and nothing else.");
            System.out.print(prompt);
            input = scanner.nextLine().trim();
        }
        return input;
    }

    // Reads a time slot confirmed to match the clinic's half-hour schedule
    private static String readTimeSlot(Scanner scanner, AppointmentManager clinic, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        while (!clinic.isClinicTimeSlot(input)) {
            String[] slots = clinic.getTimeSlots();
            System.out.println("[WARNING] Choose a clinic slot from " + slots[0] + " to "
                    + slots[slots.length - 1] + " on the half hour, e.g. 09:00 or 09:30.");
            System.out.print(prompt);
            input = scanner.nextLine().trim();
        }
        return input;
    }

    // Reads a doctor ID and re-prompts until a registered clinician is found
    private static HealthProfessional readDoctor(Scanner scanner, AppointmentManager clinic, String prompt) {
        HealthProfessional doctor = clinic.findProfessionalById(readInt(scanner, prompt, 1, 9999));
        while (doctor == null) {
            System.out.println("[WARNING] No doctor has that ID. Choose 1 to see the doctor list.");
            doctor = clinic.findProfessionalById(readInt(scanner, prompt, 1, 9999));
        }
        return doctor;
    }

    /*
     * Runs a scripted walkthrough of Part 3 and Part 6 on an isolated Demo
     * Clinic, testing valid bookings, overloaded constructors, polymorphic
     * interface dispatch, sorting, filtering and error rejections.
     */
    public static void runDemonstration() {
        AppointmentManager demo = new AppointmentManager("Demo Clinic");
        addPreconfiguredDoctors(demo);
        ArrayList<HealthProfessional> staff = demo.getStaff();

        Display.header("PART 3 - WORKING WITH HEALTH PROFESSIONALS");
        Display.subHeader("3.1 Every professional through one HealthProfessional list");
        demo.displayDoctorList();

        Display.subHeader("3.2 Capabilities shared across the hierarchy");
        for (HealthProfessional professional : staff) {
            if (professional instanceof Prescribable) {
                System.out.println("  " + demo.recordPrescription(
                        (Prescribable) professional, "Daniel Reed", "Amoxicillin 500mg"));
            }
            if (professional instanceof DiagnosticCapable) {
                System.out.println("  " + demo.recordDiagnosticOrder(
                        (DiagnosticCapable) professional, "Olivia Brown", true));
            }
        }

        Display.subHeader("3.3 Behaviour unique to each specialty and overloaded constructors");
        System.out.println("  " + ((GeneralPractitioner) demo.findProfessionalById(101)).issueReferral("Liam", "Cardiology"));
        System.out.println("  " + ((Surgeon) demo.findProfessionalById(201)).assessForSurgery("Ethan", "knee arthroscopy"));
        System.out.println("  " + ((Cardiologist) demo.findProfessionalById(301)).evaluateHeartRate("Lucas", 112));
        System.out.println("  " + ((Radiologist) demo.findProfessionalById(401)).writeImagingReport("Liam", "left knee"));
        System.out.println("  " + ((Nurse) demo.findProfessionalById(501)).takeBloodSample("Sophia", 2));
        System.out.println("  " + ((Dietitian) demo.findProfessionalById(601)).createMealPlan("Mia", 1800));

        Surgeon shortSurgeon = new Surgeon(203, "Dr. Owen Reid", "Male", "General Surgery");
        Cardiologist shortCardio = new Cardiologist(302, "Dr. Priya Patel", "Female", "Electrophysiology");
        Radiologist fullRadio = new Radiologist(402, "Dr. David Brooks", "Male",
                "CT and interventional radiology.", HealthProfessional.DEFAULT_WORKING_DAYS,
                "08:00", "16:00", "CT", "R2");
        Nurse fullNurse = new Nurse(502, "Samira Al-Mansoor", "Female",
                "Surgical recovery and triage.", HealthProfessional.DEFAULT_WORKING_DAYS,
                "08:00", "16:00", "Surgical Recovery", false);
        Dietitian shortDiet = new Dietitian(602, "Grace Foster", "Female", "APD-8820");
        Patient defaultPatient = new Patient();
        Appointment customAppointment = new Appointment(
                "APT-CUSTOM", defaultPatient, fullNurse, Appointment.TODAY, "10:00");
        AppointmentManager weekendClinic = new AppointmentManager("Weekend Express Clinic", 9, 13, 30);
        System.out.println("  " + demo.recordPrescription(shortSurgeon, "Ethan", "Paracetamol 500mg"));
        System.out.println("  " + demo.recordDiagnosticOrder(shortCardio, "Lucas", false));
        System.out.println("  " + fullRadio.getProfessionalType() + ": " + fullRadio.getName()
                + " | Equipment: " + fullRadio.getDiagnosticEquipment());
        System.out.println("  " + fullNurse.takeBloodSample("Sophia", 1)
                + " | " + demo.recordDiagnosticOrder(fullNurse, "Sophia", false));
        System.out.println("  " + shortDiet.createMealPlan("Mia", 2000));
        System.out.println("  Custom Appointment ID: " + customAppointment.getAppointmentId()
                + " | Patient: " + customAppointment.getPatient().getName()
                + " | Weekend slots: " + weekendClinic.getTimeSlots().length);

        Display.subHeader("3.4 Same staff ID means the same professional (equals)");
        GeneralPractitioner sameIdAsJenkins = new GeneralPractitioner(101, "Dr. S. Jenkins (copy)", "Female", "GP-101");
        System.out.println("  staff list contains a copy with ID 101: " + staff.contains(sameIdAsJenkins));

        Display.subHeader("3.5 Constructors reject invalid objects");
        try {
            new Surgeon(-5, "Dr. Nobody", "Male", "General Surgery");
        } catch (IllegalArgumentException e) {
            System.out.println("  Rejected: " + e.getMessage());
        }
        try {
            new Patient("P1", "Bad Phone", "Male", 30, "04-ABC", "Cough");
        } catch (IllegalArgumentException e) {
            System.out.println("  Rejected: " + e.getMessage());
        }

        Display.header("PART 6 - WORKING WITH APPOINTMENTS");
        Patient liam = new Patient("450202199506181011", "Liam O'Connor", "Male", 31, "0411222333", "Knee pain");
        Patient olivia = new Patient("450202199811232022", "Olivia Martin", "Female", 28, "0422333444", "Annual check-up");
        Patient ethan = new Patient("450202198203143033", "Ethan Walker", "Male", 44, "0433444555", "Pre-surgery review");
        Patient sophia = new Patient("450202200109054044", "Sophia Patel", "Female", 25, "0444555666", "Blood test");
        Patient lucas = new Patient("450202197007195055", "Lucas Nguyen", "Male", 56, "0455666777", "Palpitations");
        Patient mia = new Patient("450202199212086066", "Mia Thompson", "Female", 34, "0466777888", "Diabetes diet");

        Display.subHeader("6.1 Valid bookings, added out of time order");
        demo.addAppointment(liam, demo.findProfessionalById(101), "14:30");
        demo.addAppointment(olivia, demo.findProfessionalById(101), "09:00");
        demo.addAppointment(ethan, demo.findProfessionalById(201), "11:30");
        demo.addAppointment(sophia, demo.findProfessionalById(501), "08:30");
        demo.addAppointment(lucas, demo.findProfessionalById(301), "15:00");
        demo.addAppointment(liam, demo.findProfessionalById(401), "10:00");
        demo.addAppointment(mia, demo.findProfessionalById(601), "09:00");

        Display.subHeader("6.2 Invalid bookings are rejected");
        demo.addAppointment(ethan, demo.findProfessionalById(101), "09:00");   // double-booking
        demo.addAppointment(ethan, sameIdAsJenkins, "14:30");                 // double-booking via a copy
        demo.addAppointment(olivia, demo.findProfessionalById(102), "17:30");  // after clinic hours
        demo.addAppointment(olivia, demo.findProfessionalById(102), "13:15");  // not on the half hour
        demo.addAppointment(olivia, demo.findProfessionalById(201), "14:00");  // outside the surgeon's shift
        demo.addAppointment(olivia, null, "10:30");                            // no professional

        Display.subHeader("6.3 Filter by doctor and by patient");
        demo.printAppointments("Doctor 101", demo.filterByProfessionalId(101));
        demo.printAppointments("Patient 0411222333", demo.filterByPatientPhone("0411222333"));
        demo.printAppointments("Doctor 999", demo.filterByProfessionalId(999));
        demo.printAppointments("Patient 0400999888 (not found)", demo.filterByPatientPhone("0400999888"));

        Display.subHeader("6.4 Sort all, and sort a filtered subset");
        demo.printAppointments("All appointments (booking order)", demo.getAllAppointments());
        demo.printAppointments("All appointments (sorted chronologically)",
                demo.sortByTime(demo.getAllAppointments()));
        demo.printAppointments("Doctor 101 by time", demo.sortByTime(demo.filterByProfessionalId(101)));

        Display.subHeader("6.5 Cancel (patient isolation and clinic view)");
        demo.cancelMyAppointment(olivia, "11:30");   // Ethan's booking: Olivia may not cancel it
        demo.cancelMyAppointment(olivia, "09:00");
        demo.cancelAppointment(201, "11:30");
        demo.cancelAppointment("0466777888");
        demo.cancelAppointment(201, "11:30");        // already cancelled
        demo.cancelAppointment("0400999888");        // unknown patient

        Display.subHeader("6.6 Remaining appointments");
        demo.printAppointments("Remaining by time", demo.sortByTime(demo.getAllAppointments()));
    }
}
