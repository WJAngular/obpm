package cn.myapps;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import cn.myapps.core.counter.ejb.CounterProcessTest;
import cn.myapps.core.department.ejb.DepartmentProcessTest;

@RunWith(Suite.class)  
@Suite.SuiteClasses({    
    CounterProcessTest.class,
    DepartmentProcessTest.class
})  

public class AllTestSuit {

}