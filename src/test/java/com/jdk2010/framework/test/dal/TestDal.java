package com.jdk2010.framework.test.dal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.jdk2010.framework.dal.client.DalClient;

public class TestDal extends TestCase {
    private static Logger logger = LoggerFactory.getLogger(TestDal.class);

    @Test
    public void testBaseDalInsert() {
        BeanFactory factory = new ClassPathXmlApplicationContext("router/applicationContext_router.xml");
        DalClient dalClient = factory.getBean(DalClient.class);
        int result = dalClient.update("insert into student0(name,age) values ('gpp','18')");
        logger.info("影响结果：" + result + "");
    }

    @Test
    public void testBaseDalModelInsert() {
        BeanFactory factory = new ClassPathXmlApplicationContext("router/applicationContext_router.xml");
        DalClient dalClient = factory.getBean(DalClient.class);
        Student student = new Student();
        student.setName("111ykk");
        student.setAge("90");
        int result = dalClient.save(student);
        logger.info("返回id：" + result + "");
    }

    @Test
    public void testUpdate() {
        BeanFactory factory = new ClassPathXmlApplicationContext("router/applicationContext_router.xml");
        DalClient dalClient = factory.getBean(DalClient.class);
        int result = dalClient.update("update student set age='20'");
        logger.info("影响结果：" + result + "");
    }

    @Test
    public void testThreadUpdate() {
        final BeanFactory factory = new ClassPathXmlApplicationContext("dal/applicationContext.xml");
        final DalClient dalClient = factory.getBean(DalClient.class);
        ExecutorService service=Executors.newFixedThreadPool(1000);
        for (int i = 0; i < 2000; i++) {
            service.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            String age = dalClient.queryColumn("select age from student where id=1 ", "age");
                            Integer ageInt = Integer.parseInt(age) + 1;
                            logger.info(ageInt+"");
                            dalClient.update("update student set age=" + ageInt + " where id=1");
                        }
                    }
                    );
            
          
        }
        service.shutdown();
        
    }

    public static void main(String[] args) {
        new TestDal().testThreadUpdate();
    }

}
