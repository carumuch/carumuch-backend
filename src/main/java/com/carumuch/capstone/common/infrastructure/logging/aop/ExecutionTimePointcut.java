package com.carumuch.capstone.common.infrastructure.logging.aop;

import org.aspectj.lang.annotation.Pointcut;

public class ExecutionTimePointcut {

	@Pointcut("execution(* com.carumuch.capstone..application..*.*(..))")
	private void applicationPackagePointCut() {}

	@Pointcut("@target(org.springframework.stereotype.Service)")
	private void servicePointCut() {}

	@Pointcut("applicationPackagePointCut() && servicePointCut()")
	public void timeLogPointCut() {}
}
