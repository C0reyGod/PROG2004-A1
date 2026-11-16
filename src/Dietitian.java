/**
 * Dietitian class representing a nutrition therapist who neither prescribes nor
 * performs clinical diagnostics, illustrating interface segregation.
 */
public class Dietitian extends HealthProfessional {

    public static final String DEFAULT_DEPARTMENT = "Clinical Nutrition";
    public static final String DEFAULT_PROFILE = "Accredited practising dietitian.";
    public static final String DEFAULT_NUTRITION_FOCUS = "General nutrition";

    private String accreditationNumber;
    private String nutritionFocus;

    /*
     * Creates a Dietitian with full schedule, accreditation number and
     * nutrition therapy focus validated.
     */
    public Dietitian(int id, String name, String gender, String profile, String[] workingDays,
                     String shiftStart, String shiftEnd,
                     String accreditationNumber, String nutritionFocus) {
        super(id, name, gender, DEFAULT_DEPARTMENT, profile, workingDays, shiftStart, shiftEnd);
        this.accreditationNumber = requireText(accreditationNumber, "Accreditation number");
        this.nutritionFocus = requireText(nutritionFocus, "Nutrition focus");
    }

    // Creates a general clinical dietitian on the default weekday shift
    public Dietitian(int id, String name, String gender, String accreditationNumber) {
        this(id, name, gender, DEFAULT_PROFILE, DEFAULT_WORKING_DAYS,
                DEFAULT_SHIFT_START, DEFAULT_SHIFT_END, accreditationNumber, DEFAULT_NUTRITION_FOCUS);
    }

    // Returns the concrete specialty title
    @Override
    public String getProfessionalType() {
        return "Dietitian";
    }

    // Returns the dietetics clinical duty summary
    @Override
    public String getClinicalRole() {
        return "Nutrition assessment and personalised meal planning";
    }

    // Returns the dietitian's professional accreditation number
    public String getAccreditationNumber() {
        return accreditationNumber;
    }

    // Updates the accreditation number after blank-text validation
    public void setAccreditationNumber(String accreditationNumber) {
        this.accreditationNumber = requireText(accreditationNumber, "Accreditation number");
    }

    // Returns the area of nutrition therapy focus
    public String getNutritionFocus() {
        return nutritionFocus;
    }

    // Updates the nutrition focus after blank-text validation
    public void setNutritionFocus(String nutritionFocus) {
        this.nutritionFocus = requireText(nutritionFocus, "Nutrition focus");
    }

    /*
     * Dietitian-specific action: builds a daily calorie meal plan tailored to
     * this dietitian's clinical nutrition focus.
     */
    public String createMealPlan(String patientName, int dailyCalories) {
        return getName() + " created a " + dailyCalories + " kcal/day "
                + nutritionFocus + " plan for " + patientName + ".";
    }

    // Appends accreditation and nutrition focus fields to the base clinician summary
    @Override
    public String toString() {
        return super.toString()
                + Display.field("Accreditation Number", accreditationNumber)
                + Display.field("Nutrition Focus", nutritionFocus);
    }
}
