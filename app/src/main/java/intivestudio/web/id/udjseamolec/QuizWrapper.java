package intivestudio.web.id.udjseamolec;

import com.google.gson.annotations.SerializedName;

/**
 * Created by outattacker on 02/05/16.
 */
public class QuizWrapper {

    private int id;

    private String question;
    @SerializedName("image")
    private String image;
    private String answers;

    private int falseAnswer;
    private int correctAnswer;

    private String pila;
    private String pilb;
    private String pilc;
    private String pild;

    public QuizWrapper(int id, String question, String pila, String pilb, String pilc, String pild, int correctAnswer, String image) {

        this.id = id;

        this.question = question;
        this.image = image;

        this.pila = pila;
        this.pilb = pilb;
        this.pilc = pilc;
        this.pild = pild;

        this.correctAnswer = correctAnswer;

    }

    public QuizWrapper() {
        super();
    }

    public int getId() {

        return id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public String getQuestion() {

        return question;

    }

    public void setQuestion(String question) {

        this.question = question;

    }

    public int getCorrectAnswer() {

        return correctAnswer;

    }

    public int getFalseAnswer(){

        return falseAnswer;
    }

    public void setFalseAnswer(int falseAnswer){
        this.falseAnswer = falseAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {

        this.correctAnswer = correctAnswer;

    }

    public String getPila() {

        return pila;
    }

    public void setPila(String pila) {
        this.pila = pila;
    }

    public String getPilb() {
        return pilb;
    }

    public void setPilb(String pilb) {
        this.pilb = pilb;
    }

    public String getPilc() {
        return pilc;
    }

    public void setPilc(String pilc) {
        this.pilc = pilc;
    }

    public String getPild() {
        return pild;
    }

    public void setPild(String pild) {
        this.pild = pild;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
