package br.wake_in_place.connection.interfaces;

import br.wake_in_place.models.ErrorResponse;

/**
 * Created by Jeferson on 01/05/2017.
 */

public interface OnError<T> {
    public void onErrorResponse(ErrorResponse response);
}
