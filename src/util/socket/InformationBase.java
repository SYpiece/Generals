package util.socket;

import java.util.Objects;

public abstract class InformationBase implements Information {
    protected final boolean isQuestion, isAnswer;
    protected final Information another;

    public boolean isQuestion() {
        return isQuestion;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public Information getAnother() {
        return another;
    }

    protected InformationBase(boolean isQuestion, boolean isAnswer, Information another) {
        this.isQuestion = isQuestion;
        this.isAnswer = isAnswer;
        if (isAnswer) {
            this.another = Objects.requireNonNull(another);
        } else {
            this.another = null;
        }
    }
}
