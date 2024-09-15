package com.tatakai.parrotojbackend.service.impl;

import cn.hutool.json.JSONUtil;
import com.tatakai.parrotojbackend.codeSandbox.CodeSandboxFactory;
import com.tatakai.parrotojbackend.codeSandbox.CodeSandboxProxy;
import com.tatakai.parrotojbackend.exception.BusinessException;
import com.tatakai.parrotojbackend.model.domain.Question;
import com.tatakai.parrotojbackend.model.domain.QuestionSubmit;
import com.tatakai.parrotojbackend.model.dto.codeSandbox.ExecuteCodeRequest;
import com.tatakai.parrotojbackend.model.dto.codeSandbox.ExecuteCodeResponse;
import com.tatakai.parrotojbackend.model.dto.question.JudgeCase;
import com.tatakai.parrotojbackend.model.dto.questionsubmit.JudgeContext;
import com.tatakai.parrotojbackend.model.dto.questionsubmit.JudgeInfo;
import com.tatakai.parrotojbackend.model.enums.QuestionSubmitLanguageEnum;
import com.tatakai.parrotojbackend.model.enums.QuestionSubmitStatusEnum;
import com.tatakai.parrotojbackend.result.ErrorCode;
import com.tatakai.parrotojbackend.service.JudgeService;
import com.tatakai.parrotojbackend.service.QuestionService;
import com.tatakai.parrotojbackend.service.QuestionSubmitService;
import com.tatakai.parrotojbackend.strategy.judge.JudgeManager;
import com.tatakai.parrotojbackend.utils.ThrowUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionService questionService;
    @Value("${code-sandbox.type}")
    private String type;
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1. 从数据库中获取题目提交信息
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        // 2. 校验参数
        ThrowUtil.throwIf(questionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String judgeInfo = questionSubmit.getJudgeInfo();
        Integer status = questionSubmit.getStatus();
        Long questionId = questionSubmit.getQuestionId();
        Long userId = questionSubmit.getUserId();
        Date createTime = questionSubmit.getCreateTime();
        Date updateTime = questionSubmit.getUpdateTime();
        Integer isDelete = questionSubmit.getIsDelete();
        if (QuestionSubmitLanguageEnum.getEnumByValue(language) == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "语言参数错误");
        }
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到题目信息！");
        }
        if (!QuestionSubmitStatusEnum.WAITING.getValue().equals(status)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已在判题中或已判题结束，无需重复提交！");
        }

        // 3. 更新题目状态
        QuestionSubmit updateSubmit = new QuestionSubmit();
        updateSubmit.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        updateSubmit.setId(questionSubmitId);
        boolean res = questionSubmitService.updateById(questionSubmit);
        if (!res) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新题目状态失败！");
        }
        // 4. 调用代码沙箱
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder().code(code).inputList(inputList).language(language).build();
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(CodeSandboxFactory.newInstance(type));
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode(executeCodeRequest);
        // 5. 判题
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setExecuteCodeResponse(executeCodeResponse);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeManager judgeManager = new JudgeManager();
        JudgeInfo judgeRes = judgeManager.doJudge(judgeContext);

        // 6. 更新数据库中提交表
        QuestionSubmit result=new QuestionSubmit();
        result.setId(questionSubmitId);
        result.setJudgeInfo(JSONUtil.toJsonStr(judgeRes));
        result.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitService.updateById(result);

        return result;
    }
}
