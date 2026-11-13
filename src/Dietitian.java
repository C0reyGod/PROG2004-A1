/**
 * A Dietitian: plans clinical nutrition. Neither prescribes nor runs
 * diagnostic tests, which is why those abilities are interfaces rather
 * than methods on the base class.
 *
 * @author kwu
 * @version 3.0
 */
public class Dietitian extends HealthProfessional {

    private String accreditationNumber;
    private String nutritionFocus;

    /**
     * Creates a dietitian with every detail specified.
     *
     * @param id                  positive staff ID
     * @param name                full name
     * @param profile             biography and expertise
     * @param workingDays         weekdays worked
     * @param shiftStart          first bookable slot
     * @param shiftEnd            last bookable slot
     * @param accreditationNumber professional accreditation number
     * @param nutritionFocus      area of nutrition therapy
     */
    public Dietitian(int id, String name, String profile, String[] workingDays,
                     String shiftStart, String shiftEnd,
                     String accreditationNumber, String nutritionFocus) {
        super(id, name, "Clinical Nutrition", profile, workingDays, shiftStart, shiftEnd);
        this.accreditationNumber = requireText(accreditationNumber, "Accreditation number");
        this.nutritionFocus = requireText(nutritionFocus, "Nutrition focus");
    }

    /**
     * Creates a general clinical dietitian on the default weekday shift.
     *
     * @param id                  positive staff ID
     * @param name                full name
     * @param accreditationNumber professional accreditation number
     */
    public Dietitian(int id, String name, String accreditationNumber) {
        this(id, name, "Accredited practising dietitian.", DEFAULT_WORKING_DAYS,
                DEFAULT_SHIFT_START, DEFAULT_SHIFT_END, accreditationNumber, "General nutrition");
    }

    @Override
    public String getProfessionalType() {
        return "Dietitian";
    }

    @Override
    public String getClinicalRole() {
        return "Nutrition assessment and personalised meal planning";
    }

    /** @return the accreditation number */
    public String getAccreditationNumber() {
        return accreditationNumber;
    }

    /** @param accreditationNumber the new accreditation number */
    public void setAccreditationNumber(String accreditationNumber) {
        this.accreditationNumber = requireText(accreditationNumber, "Accreditation number");
    }

    /** @return the nutrition focus */
    public String getNutritionFocus() {
        return nutritionFocus;
    }

    /** @param nutritionFocus the new nutrition focus */
    public void setNutritionFocus(String nutritionFocus) {
        this.nutritionFocus = requireText(nutritionFocus, "Nutrition focus");
    }

    /**
     * Creates a daily meal plan.
     *
     * @param patientName   the patient's name
     * @param dailyCalories target calories per day
     * @return a description of the plan
     */
    public String createMealPlan(String patientName, int dailyCalories) {
        return getName() + " created a " + dailyCalories + " kcal/day "
                + nutritionFocus + " plan for " + patientName + ".";
    }

    @Override
    public String toString() {
        return super.toString()
                + Display.field("Accreditation Number", accreditationNumber)
                + Display.field("Nutrition Focus", nutritionFocus);
    }
}
