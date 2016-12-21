package ru.vkulakov.phonex.resources;

import ru.vkulakov.phonex.model.Phone;
import ru.vkulakov.phonex.model.Result;
import ru.vkulakov.phonex.services.PhoneService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("search/{phone}")
public class SearchResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Result search(@PathParam("phone") String phoneStr) {
    	if(phoneStr == null || phoneStr.trim().isEmpty()) {
    		return new Result(Result.EMPTY, "Номер телефона не передан");
		}

    	if(!phoneStr.matches("^7\\d{10}$")) {
			return new Result(Result.BAD_FORMAT, "Передан некорректный номер телефона");
		}

		try {
			Phone phone = new PhoneService().search(phoneStr);
			if(phone == null) {
				return new Result(Result.NOT_FOUND, "Информация по номеру телефона не найдена");
			} else {
				return new Result(phone);
			}
		} catch (Exception e) {
    		System.err.println("Ошибка поиска информации по номеру телефона");
    		e.printStackTrace(System.err);

			return new Result(Result.ERROR, "Внутренняя ошибка на сервере");
		}
    }
}
