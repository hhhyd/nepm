package com.neusoft.nepm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
1.前端发起请求，参数封装到入参DTO传给控制层（controller）
2.控制层调用业务层（service），并把入参DTO传给业务层
3.业务层把DTO参数传给mapper，mapper用PO和数据库交互查询
4.mapper查出PO的原始数据，service做业务处理，统计汇总，数据组装
5.service把处理后的数据组装成出参DTO返回给控制层（controller）
6.控制层把出参DTO返回给前端，前端渲染页面
*/
@MapperScan("com.neusoft.nepm.mapper")
@SpringBootApplication
public class NepmserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(NepmserverApplication.class, args);
	}

}
