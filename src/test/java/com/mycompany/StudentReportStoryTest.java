package com.mycompany;

import java.io.File;

import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.ManagedPages;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.pages.Pages;
import net.thucydides.junit.runners.ThucydidesRunner;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.mycompany.listeners.ArquillianEnricher;
import com.mycompany.requirements.Application;
import com.mycompany.steps.EndUserSteps;

@Story(Application.StudentReport.ListStudents.class)
@RunWith(ThucydidesRunner.class)
public class StudentReportStoryTest {
    @Rule
    public ArquillianEnricher enricher = new ArquillianEnricher();
    
//    @Drone
//    private WebDriver driver;
    
//    @Managed(uniqueSession = true)
//    public WebDriver webdriver;

//    @ManagedPages(defaultUrl = "http://localhost:8080/test/studentReport.xhtml")
//    public Pages pages;

    @Steps
    public EndUserSteps endUser;
    
    @Deployment(testable=false)
	public static WebArchive createTestArchive() {
		WebArchive archive = ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addPackage("com.mycompany.domain")
				.addPackage("com.mycompany.web.model")
				.setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
				.addAsWebResource(new File("src/main/webapp/studentReport.xhtml"))
				.addAsWebInfResource(new File("src/main/webapp/WEB-INF/faces-config.xml"))
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		return archive;
	}

    @Test
    public void list_of_students_should_not_be_empty() {
        endUser.list_students();
    }
} 