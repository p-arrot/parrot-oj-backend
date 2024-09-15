package com.tatakai.parrotojbackend.strategy.judge;

import com.tatakai.parrotojbackend.model.domain.QuestionSubmit;
import com.tatakai.parrotojbackend.model.dto.questionsubmit.JudgeContext;
import com.tatakai.parrotojbackend.model.dto.questionsubmit.JudgeInfo;
import com.tatakai.parrotojbackend.model.enums.QuestionSubmitLanguageEnum;

public class JudgeManager implements JudgeStrategy{

    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        String java = QuestionSubmitLanguageEnum.JAVA.getValue();
        JudgeStrategy judgeStrategy = null;
        if(java.equals(language)){
            judgeStrategy = new JavaJudge();
        }else{
            judgeStrategy = new DefaultJudge();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
