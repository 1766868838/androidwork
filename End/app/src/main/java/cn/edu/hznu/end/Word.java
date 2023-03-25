package cn.edu.hznu.end;

public class Word {
    int state;//状态
    String word;
    String phonetic;//音标
    String eVoice;//英音
    String aVoice;
    String explanation;
    int id;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String geteVoice() {
        return eVoice;
    }

    public void seteVoice(String eVoice) {
        this.eVoice = eVoice;
    }

    public String getaVoice() {
        return aVoice;
    }

    public void setaVoice(String aVoice) {
        this.aVoice = aVoice;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public int getId() {
        return id;
    }

    public Word(String word, String phonetic, String eVoice, String aVoice, String explanation, int id) {
        this.word = word;
        this.phonetic = phonetic;
        this.eVoice = eVoice;
        this.aVoice = aVoice;
        this.explanation = explanation;
        this.id = id;
        this.state = 0;
    }
    public Word(int id,String word,  String eVoice, String aVoice, String explanation) {
        this.word = word;
        this.phonetic = phonetic;
        this.eVoice = eVoice;
        this.aVoice = aVoice;
        this.explanation = explanation;
        this.id = id;
        this.state = 0;
    }
    public Word(String word, String explanation) {
        this.word = word;

        this.explanation = explanation;
    }
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setId(int id) {
        this.id = id;
    }
}
