package com.mycompany.fragments;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.mycompany.web.model.Student;

public class StudentViewFragment {
    @FindBy(xpath = "./td[1]")
    private WebElement firstname;
    
    @FindBy(xpath = "./td[2]")
    private WebElement lastname;

    @FindBy(xpath = "./td[3]")
    private WebElement points;
    
    public Student get() {
        Student current = new Student();
        current.setFirstname(firstname.getText());
        current.setLastname(lastname.getText());
        current.setPoints(Integer.parseInt(points.getText()));
        return current;
    }
}
