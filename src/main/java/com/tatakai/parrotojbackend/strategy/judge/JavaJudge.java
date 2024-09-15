package com.tatakai.parrotojbackend.strategy.judge;

import cn.hutool.json.JSONUtil;
import com.tatakai.parrotojbackend.model.domain.Question;
import com.tatakai.parrotojbackend.model.domain.QuestionSubmit;
import com.tatakai.parrotojbackend.model.dto.codeSandbox.ExecuteCodeResponse;
import com.tatakai.parrotojbackend.model.dto.question.JudgeCase;
import com.tatakai.parrotojbackend.model.dto.question.JudgeConfig;
import com.tatakai.parrotojbackend.model.dto.questionsubmit.JudgeContext;
import com.tatakai.parrotojbackend.model.dto.questionsubmit.JudgeInfo;
import com.tatakai.parrotojbackend.model.enums.JudgeInfoMessageEnum;

import java.util.List;

public class JavaJudge implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        ExecuteCodeResponse executeCodeResponse = judgeContext.getExecuteCodeResponse();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        List<String> outputList = executeCodeResponse.getOutputList();
        String message = executeCodeResponse.getMessage();
        Integer status = executeCodeResponse.getStatus();

        JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();
        JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        JudgeInfo res = new JudgeInfo();


        // 判断输出数目与用例数目是否一致
        if (outputList.size() != judgeCaseList.size()) {
            res.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getText());
            return res;
        }
        // 判断输出结果
        for (int i = 0; i < outputList.size(); i++) {
            if (!judgeCaseList.get(i).getOutput().equals(outputList.get(i))) {
                res.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER.getText());
                return res;
            }
        }
        // 判断题目限制
        res.setTime(judgeInfo.getTime());
        res.setMemory(judgeInfo.getMemory());
        if (judgeInfo.getMemory() > judgeConfig.getMemoryLimit()) {
            res.setMessage(JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED.getText());
            return res;
        }
        if (judgeInfo.getTime() > judgeConfig.getTimeLimit()) {
            res.setMessage(JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED.getText());
            return res;
        }

        res.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        return res;
    }
}