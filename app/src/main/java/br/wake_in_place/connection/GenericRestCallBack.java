package br.wake_in_place.connection;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.wake_in_place.connection.interfaces.OnError;
import br.wake_in_place.connection.interfaces.OnErrorServer;
import br.wake_in_place.connection.interfaces.OnSucess;
import br.wake_in_place.models.ErrorResponse;
import br.wake_in_place.utils.ConnectionChecker;
import br.wake_in_place.utils.DialogCustomUtil;
import br.wake_in_place.utils.DialogSingleton;
import br.wake_in_place.utils.Log;
import br.wake_in_place.utils.Snackbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GenericRestCallBack<T> implements Callback<T> {
    private OnSucess onSucessListener;
    private OnError onErrorListener;
    private OnErrorServer onErrorServer;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtNothing;
    private Context mContext;
    private Call<T> mCall;
    private boolean isBlocked;
    private ProgressDialog progressDialog;

    private Context getContext() {
        return mContext;
    }

    private void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void request(Context context, Call<T> call) {
        doRequest(context, call);
    }

    public void request(Context context, Call<T> call, ProgressBar progressBar) {
        this.progressBar = progressBar;
        doRequest(context, call);
    }

    public void request(Context context, Call<T> call, OnSucess onSucessListener) {
        this.onSucessListener = onSucessListener;
        doRequest(context, call);
    }

    public void request(Context context, Call<T> call, OnSucess onSucessListener, boolean isBlocked) {
        this.onSucessListener = onSucessListener;
        this.isBlocked = isBlocked;
        doRequestBlock(context, call);
    }

    public void request(Context context, Call<T> call, OnSucess onSucessListener, OnError onErrorListener, boolean isBlocked) {
        this.onSucessListener = onSucessListener;
        this.onErrorListener = onErrorListener;
        this.isBlocked = isBlocked;
        doRequestBlock(context, call);
    }

    public void request(Context context, Call<T> call, OnSucess onSucessListener, OnError onErrorListener) {
        this.onSucessListener = onSucessListener;
        this.onErrorListener = onErrorListener;
        doRequest(context, call);
    }

    public void request(Context context, Call<T> call, OnSucess onSucessListener, ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.onSucessListener = onSucessListener;
        doRequest(context, call);
    }

    public void request(Context context, Call<T> call, OnSucess onSucessListener, SwipeRefreshLayout swipeRefreshLayout, TextView txtNothing) {
        this.onSucessListener = onSucessListener;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.txtNothing = txtNothing;
        doRequest(context, call);
    }

    public void request(Context context, Call<T> call, OnSucess onSucessListener, View progress, TextView txtNothing) {
        if (progress instanceof ProgressBar) {
            this.progressBar = (ProgressBar) progress;
        } else if (progress instanceof SwipeRefreshLayout) {
            this.swipeRefreshLayout = (SwipeRefreshLayout) progress;
        }
        this.onSucessListener = onSucessListener;
        this.txtNothing = txtNothing;
        doRequest(context, call);
    }

    public void request(Context context, Call<T> call, OnSucess onSucessListener, OnError onErrorListener, View progress, TextView txtNothing) {
        if (progress instanceof ProgressBar) {
            this.progressBar = (ProgressBar) progress;
        } else if (progress instanceof SwipeRefreshLayout) {
            this.swipeRefreshLayout = (SwipeRefreshLayout) progress;
        }
        this.onSucessListener = onSucessListener;
        this.onErrorListener = onErrorListener;
        this.txtNothing = txtNothing;
        doRequest(context, call);
    }


    public void request(Context context, Call<T> call, OnSucess onSucessListener, SwipeRefreshLayout swipeRefreshLayout) {
        this.onSucessListener = onSucessListener;
        this.swipeRefreshLayout = swipeRefreshLayout;
        doRequest(context, call);
    }

    public void request(Context context, Call<T> call, OnSucess onSucessListener, OnError onErrorListener, ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.onErrorListener = onErrorListener;
        this.onSucessListener = onSucessListener;
        doRequest(context, call);
    }

    public void request(Context context, Call<T> call, OnSucess onSucessListener, OnErrorServer onErrorServer, ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.onErrorServer = onErrorServer;
        this.onSucessListener = onSucessListener;
        doRequest(context, call);
    }

    private void progressBar(boolean isVisible) {
        if (progressBar != null) {
            progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(isVisible);
        }

    }

    private void txtNothing(boolean isVisible) {
        if (txtNothing != null) {
            txtNothing.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    private void setProgressdialog(boolean isVisible) {
        if (isVisible) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Aguarde");
            progressDialog.setMessage("Carregando...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        } else {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    private void doRequest(Context context, Call<T> call) {
        progressBar(true);
        setContext(context);
        mCall = call;
        if (ConnectionChecker.checkConnection(context)) {
            call.enqueue(this);
        } else {
            progressBar(false);
        }

    }

    private void doRequestBlock(Context context, Call<T> call) {
        setContext(context);
        mCall = call;
        setProgressdialog(true);
        if (ConnectionChecker.checkConnection(context)) {
            call.enqueue(this);
        } else {
            setProgressdialog(false);
        }

    }

    private void requestLastCall() {
        progressBar(true);
        setProgressdialog(isBlocked);
        if (mContext != null && ConnectionChecker.checkConnection(mContext)) {
            Call<T> newCall = mCall.clone();
            newCall.enqueue(this);
        } else {
            progressBar(false);
            setProgressdialog(false);
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try {
            if (response.isSuccessful()) {
                txtNothing(false);
                if (onSucessListener != null) {
                    onSucessListener.onSucessResponse(response);
                }
            } else {
                txtNothing(true);
                if (onErrorListener != null) {
                    onErrorListener.onErrorResponse(ErrorResponse.getResponseError(response.errorBody(), response.code()));
                } else if (onErrorServer != null) {
                    onErrorServer.onErrorResponse(response.errorBody());
                } else {
                    errorResponse(response);
                    Log.e(response.errorBody().toString());
                }

            }
            progressBar(false);
            setProgressdialog(false);
        }catch (Exception e){
            Log.e(e.getMessage()!= null?e.getMessage():"Error");
        }

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        progressBar(false);
        setProgressdialog(false);
        txtNothing(true);
        if (onErrorListener != null) {
            onErrorListener.onErrorResponse(new ErrorResponse());
        } else {
            errorResponse(null);
        }
    }

    private void errorResponse(Response<T> response) {
        if (response == null) {
            onError(ErrorResponse.getGsonParseError());
        } else if (response.code() >= 500) {
            onError(ErrorResponse.getExceptionFail(response.errorBody(), response.code()));
        } else if (response.code() == 403) {
            onError(ErrorResponse.getUpdateApplicationError());
        } else if (response.code() == 401) {
            onError(ErrorResponse.setSessionError(response.errorBody(), response.code()));
        } else {
            onError(ErrorResponse.getResponseError(response.errorBody(), response.code()));
        }
    }

    private void onError(ErrorResponse error) {
        DialogCustomUtil.OnItemClick onItemClickRepeat = new DialogCustomUtil.OnItemClick() {
            @Override
            public void onItemClick(View view) {
                requestLastCall();
            }
        };
        if (error.getCode() != null) {
            switch (error.getCode()) {
                case ErrorResponse.UPDATE_VERSION:
                    DialogSingleton.getInstance().dialog(getContext(), ErrorResponse.UPDATE_VERSION_TITLE, error.getMessage(), false, null);
                    break;
                case ErrorResponse.UNEXPECTED:
                    DialogSingleton.getInstance().dialog(getContext(), ErrorResponse.UNEXPECTED_TITLE, error.getMessageServer(), true, onItemClickRepeat);
                    break;
                case ErrorResponse.NETWORK_DISABLE:
                    DialogSingleton.getInstance().dialog(getContext(), ErrorResponse.NETWORK_DISABLE_TITLE, error.getMessage(), false, null);
                    break;
                case ErrorResponse.SESSION:
                    DialogSingleton.getInstance().dialog(getContext(), ErrorResponse.SESSION_TITLE, error.getMessage(), new DialogCustomUtil.OnItemClick() {
                        @Override
                        public void onItemClick(View view) {
                            //logout
                        }
                    });
                    break;
                case ErrorResponse.FAIL:
                    DialogSingleton.getInstance().dialog(getContext(), ErrorResponse.FAIL_TITLE, error.getMessage(), true, onItemClickRepeat);
                    break;
                default:
                    Snackbar.make(getContext(), error.getMessage());
                    break;
            }
        } else {
            Snackbar.make(getContext(), error.getMessage());
        }
    }



}

