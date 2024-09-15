package com.tatakai.parrotojbackend.service;

import com.tatakai.parrotojbackend.model.domain.QuestionSubmit;

public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
