package com.tatakai.parrotojbackend.strategy.judge;

import com.tatakai.parrotojbackend.model.dto.questionsubmit.JudgeContext;
import com.tatakai.parrotojbackend.model.dto.questionsubmit.JudgeInfo;

public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
