package com.mycompany.pages;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

import java.util.List;

import net.thucydides.core.pages.PageObject;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Predicate;
import com.mycompany.fragments.StudentViewFragment;
import com.mycompany.web.model.Student;

//@DefaultUrl("http://localhost:8080/test/exchangeCurrencies.xhtml")
public class StudentReportPage extends PageObject {
    @Drone
    WebDriver driver;

    @FindBy(xpath="//table/tbody/tr[/td]")
	private List<WebElement> rows;
    
	public StudentReportPage(WebDriver driver) {
		super(driver);
	}

	public void list_students() {
	    waitGui().until(new Predicate<WebDriver>() {

            @Override
            public boolean apply(WebDriver driver) {
                return rows.size() > 0;
            }});
	    waitGui().until().element(rows.get(0)).is().visible();
	    for (WebElement row: rows) {
	        StudentViewFragment studentView = Graphene.createPageFragment(StudentViewFragment.class, row);
	        Student student = studentView.get();
	    }
	}
}


