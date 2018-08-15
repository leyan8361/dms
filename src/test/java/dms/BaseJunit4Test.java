package dms;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class) // ʹ��junit4���в���
@ContextConfiguration(locations = { "classpath:applicationContext.xml" }) // ���������ļ�
@Rollback(value = true) // Ĭ�ϲ���������ݿ�����ع�
@Transactional(transactionManager = "transactionManager")
public class BaseJunit4Test {

}