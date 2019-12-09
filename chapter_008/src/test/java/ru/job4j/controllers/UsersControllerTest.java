package ru.job4j.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import ru.job4j.crudservlet.ValidationService;
import ru.job4j.crudservlet.ValidationStub;
import ru.job4j.filtersecurity.Role;
import ru.job4j.servlet.Validation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ValidationService.class)
public class UsersControllerTest {

    @Test
    public void whenAddUserThenStoreIt() throws ServletException, IOException {

//        Validation validation =  mock(ValidationStub.class);
//        PowerMockito.mock(ValidationService.class);

//        Validation<User> validation = PowerMockito.mock(ValidationService.class);
//        Whitebox.setInternalState(ValidationStub.class, "INSTANCE", validation);

//        Validation<User> validation = Mockito.mock(Validation.class);
//        Whitebox.setInternalState(ValidationService.INSTANCE, ValidationStub.INSTANCE);

//        Mockito.when(ValidationService.class).thenReturn(validation);

//        Validation<User> validation = PowerMockito.mock(ValidationService.class);

//        Validation<User> validation = ValidationStub.INSTANCE;
//        PowerMockito.mock(ValidationService.class);

        Validation stub = ValidationStub.INSTANCE;
        ValidationService validation = PowerMockito.mock(ValidationService.class);
        Whitebox.setInternalState(validation, "name", "C");
        Whitebox.setInternalState(validation, "ordinal", 2);

//        PowerMockito.mock(ValidationService.class);
//        PowerMockito.when(ValidationService.values()).thenReturn(new Validation[]{stub.A, stub.B, C});
        PowerMockito.when(ValidationService.INSTANCE.add(any())).thenReturn(Optional.of(1));

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getParameter("name")).thenReturn("Petr");

        when(req.getParameter("action")).thenReturn("create");
        when(req.getParameter("role")).thenReturn(Role.USER.name());

         new UsersController().doPost(req, resp);
         assertThat(validation.findAll().iterator().next().getName(), is(""));
    }
}

/*
@RunWith(PowerMockRunner.class)
public class BarTest {

    private Bar bar;

    @Before
    public void createBar() {
        bar = new Bar();
    }

    @Test(expected = IllegalArgumentException.class)
    @PrepareForTest(MyEnum.class)
    public void unknownValueShouldThrowException() throws Exception {
        MyEnum C = PowerMockito.mock(MyEnum.class);
        Whitebox.setInternalState(C, "name", "C");
        Whitebox.setInternalState(C, "ordinal", 2);

        PowerMockito.mockStatic(MyEnum.class);
        PowerMockito.when(MyEnum.values()).thenReturn(new MyEnum[]{MyEnum.A, MyEnum.B, C});

        bar.foo(C);
    }

    @Test
    public void AShouldReturn1() {
        assertEquals(1, bar.foo(MyEnum.A));
    }

    @Test
    public void BShouldReturn2() {
        assertEquals(2, bar.foo(MyEnum.B));
    }
}
*/
