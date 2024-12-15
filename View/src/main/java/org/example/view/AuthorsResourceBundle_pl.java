package org.example.view;

import java.util.ListResourceBundle;

public class AuthorsResourceBundle_pl extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"author1", "Kacper T. Błaszczyk"},
                {"author2", "Maciej Miazek"}
        };
    }
}
