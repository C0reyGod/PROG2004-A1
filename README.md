# Health Service Appointment System

A console-based Java application for managing health professionals, patient registrations, capability-based clinical orders, and same-day clinic appointments.

## Features

- **Health Professional Hierarchy**: Abstract `HealthProfessional` base class extended by six concrete specialties (`GeneralPractitioner`, `Surgeon`, `Cardiologist`, `Radiologist`, `Nurse`, and `Dietitian`), plus cross-hierarchy capability interfaces (`Prescribable` and `DiagnosticCapable`).
- **Capability Contracts**: `Prescribable` defines prescription issuance, restricted-schedule eligibility (`canPrescribeRestrictedMedication`), and supply limits (`getMaxSupplyDays`); `DiagnosticCapable` defines diagnostic procedures, equipment summaries (`getDiagnosticEquipment`), and urgent/routine turnaround estimates (`estimateTurnaroundMinutes`).
- **Demographic & Schedule Validation**: Validates required text, gender options (`Male`, `Female`, `Other`, `Not specified`), age bounds (`1-120`), 8-15 digit mobile numbers, shift hours (`shiftStart`-`shiftEnd`), 30-minute clinic slots (`08:00`-`16:00` by default), and double-booking constraints.
- **Appointment Management**: Supports filtering by doctor ID or patient mobile number, chronological sorting via `Comparable<Appointment>`, patient-scoped viewing/cancellation, clinic-wide cancellation, and interface-typed `recordPrescription` / `recordDiagnosticOrder` helpers.
- **Interactive Console & Automated Demo**: Looping menu (`0-8`) with validated keyboard input alongside a built-in end-to-end demonstration (Option `8`).

## Project Structure

```text
.
|-- .gitignore
|-- README.md
+-- src/
    |-- Display.java               # Console field alignment, headers, and array formatting
    |-- HealthProfessional.java    # Abstract base class for clinical staff
    |-- Prescribable.java          # Prescription capability interface and supply rules
    |-- DiagnosticCapable.java     # Diagnostic procedure interface and turnaround rules
    |-- GeneralPractitioner.java   # GP subclass (Prescribable, DiagnosticCapable)
    |-- Surgeon.java               # Surgeon subclass (Prescribable)
    |-- Cardiologist.java          # Cardiologist subclass (Prescribable, DiagnosticCapable)
    |-- Radiologist.java           # Radiologist subclass (DiagnosticCapable)
    |-- Nurse.java                 # Nurse subclass (DiagnosticCapable)
    |-- Dietitian.java             # Dietitian subclass
    |-- Patient.java               # Patient details, validation, and bookings
    |-- Appointment.java           # Immutable booking record with time ordering
    |-- AppointmentManager.java    # Clinic staff, slots, booking, filtering, sorting, orders
    +-- AssignmentOne.java         # Main entry point, console menu, and demonstration
```

## Compile and Run

From the project root directory:

```bash
mkdir -p bin
javac -d bin src/*.java
java -cp bin AssignmentOne
```

Follow the on-screen menu prompts (`1-7` for interactive booking and clinic management, `8` for the automated demonstration, `0` to exit).
