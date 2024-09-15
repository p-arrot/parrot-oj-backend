package com.tatakai.parrotojbackend.model.dto.questionsubmit;

import com.tatakai.parrotojbackend.model.domain.Question;
import com.tatakai.parrotojbackend.model.domain.QuestionSubmit;
import com.tatakai.parrotojbackend.model.dto.codeSandbox.ExecuteCodeResponse;
import com.tatakai.parrotojbackend.model.dto.question.JudgeCase;
import lombok.Data;

import java.util.List;
@Data
public class JudgeContext {
    private ExecuteCodeResponse executeCodeResponse;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
