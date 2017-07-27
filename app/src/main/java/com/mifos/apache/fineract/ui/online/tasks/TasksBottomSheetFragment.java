package com.mifos.apache.fineract.ui.online.tasks;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mifos.apache.fineract.R;
import com.mifos.apache.fineract.data.models.customer.Command;
import com.mifos.apache.fineract.data.models.customer.Customer;
import com.mifos.apache.fineract.ui.base.MifosBaseActivity;
import com.mifos.apache.fineract.ui.base.MifosBaseBottomSheetDialogFragment;
import com.mifos.apache.fineract.ui.base.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 27/07/17.
 */
public class TasksBottomSheetFragment extends MifosBaseBottomSheetDialogFragment
        implements TasksBottomSheetContract.View {

    @BindView(R.id.iv_task1)
    ImageView ivTask1;

    @BindView(R.id.iv_task2)
    ImageView ivTask2;

    @BindView(R.id.tv_task1)
    TextView tvTask1;

    @BindView(R.id.tv_task2)
    TextView tvTask2;

    @BindView(R.id.ll_task2)
    LinearLayout llTask2;

    @BindView(R.id.ll_task_list)
    LinearLayout llTaskList;

    @BindView(R.id.ll_task_form)
    LinearLayout llTaskForm;

    @BindView(R.id.tv_header)
    TextView tvHeader;

    @BindView(R.id.tv_sub_header)
    TextView tvSubHeader;

    @BindView(R.id.et_comment)
    EditText etComment;

    @BindView(R.id.btn_submit_task)
    Button btnSubmitTask;

    @Inject
    TasksBottomSheetPresenter tasksBottomSheetPresenter;

    View rootView;

    private BottomSheetBehavior behavior;
    private Customer.State state;
    private Command command;
    private String customerIdentifier;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        rootView = View.inflate(getContext(), R.layout.bottom_sheet_task_list, null);
        dialog.setContentView(rootView);
        behavior = BottomSheetBehavior.from((View) rootView.getParent());
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        tasksBottomSheetPresenter.attachView(this);
        ButterKnife.bind(this, rootView);
        command = new Command();

        switch (state) {
            case ACTIVE:
                ivTask1.setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.ic_lock_black_24dp));
                tvTask1.setText(getString(R.string.lock));
                ivTask2.setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_black_24dp));
                tvTask2.setText(getString(R.string.close));
                break;
            case PENDING:
                llTask2.setVisibility(View.GONE);
                ivTask1.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                        R.drawable.ic_check_circle_black_24dp));
                ivTask1.setColorFilter(ContextCompat.getColor(getActivity(), R.color.status));
                tvTask1.setText(getString(R.string.activate));
                break;
            case LOCKED:
                ivTask1.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                        R.drawable.ic_lock_open_black_24dp));
                ivTask1.setColorFilter(ContextCompat.getColor(getActivity(), R.color.status));
                tvTask1.setText(getString(R.string.un_lock));
                ivTask2.setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_black_24dp));
                tvTask2.setText(getString(R.string.close));
                break;
            case CLOSED:
                llTask2.setVisibility(View.GONE);
                ivTask1.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                        R.drawable.ic_check_circle_black_24dp));
                ivTask1.setColorFilter(ContextCompat.getColor(getActivity(), R.color.status));
                tvTask1.setText(getString(R.string.reopen));
                break;
        }

        return dialog;
    }

    @OnClick(R.id.iv_task1)
    void onClickTask1() {
        switch (state) {
            case ACTIVE:
                command.setAction(Command.Action.LOCK.name());
                tvHeader.setText(getString(R.string.lock));
                tvSubHeader.setText(
                        getString(R.string.please_verify_following_task, getString(R.string.lock)));
                btnSubmitTask.setText(getString(R.string.lock));
                break;
            case PENDING:
                command.setAction(Command.Action.ACTIVATE.name());
                tvHeader.setText(getString(R.string.activate));
                tvSubHeader.setText(getString(R.string.please_verify_following_task,
                        getString(R.string.activate)));
                btnSubmitTask.setText(getString(R.string.activate));
                break;
            case LOCKED:
                command.setAction(Command.Action.UNLOCK.name());
                tvHeader.setText(getString(R.string.un_lock));
                tvSubHeader.setText(getString(R.string.please_verify_following_task,
                        getString(R.string.un_lock)));
                btnSubmitTask.setText(getString(R.string.un_lock));
                break;
            case CLOSED:
                command.setAction(Command.Action.REOPEN.name());
                tvHeader.setText(getString(R.string.reopen));
                tvSubHeader.setText(getString(R.string.please_verify_following_task,
                        getString(R.string.reopen)));
                btnSubmitTask.setText(getString(R.string.reopen));
                break;
        }
        llTaskList.setVisibility(View.GONE);
        llTaskForm.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_task2)
    void onClickTask2() {
        command.setAction(Command.Action.CLOSE.name());
        tvHeader.setText(getString(R.string.close));
        tvSubHeader.setText(getString(R.string.please_verify_following_task,
                getString(R.string.close)));
        btnSubmitTask.setText(getString(R.string.close));

        llTaskList.setVisibility(View.GONE);
        llTaskForm.setVisibility(View.VISIBLE);
    }

    public void setCustomerStatus(Customer.State customerStatus) {
        state = customerStatus;
    }

    public void setCustomerIdentifier(String identifier) {
        customerIdentifier = identifier;
    }

    @OnClick(R.id.btn_submit_task)
    void submitTask() {
        command.setComment(etComment.getText().toString().trim());
        etComment.setEnabled(false);
        tasksBottomSheetPresenter.changeCustomerStatus(customerIdentifier, command);
    }

    @OnClick(R.id.btn_cancel)
    void onCancel() {
        dismiss();
    }

    @Override
    public void statusChangedSuccessfully() {
        Toaster.show(rootView, getString(R.string.task_updated_successfully));
        dismiss();
    }

    @Override
    public void showProgressbar() {
        showMifosProgressDialog(getString(R.string.updating_status));
    }

    @Override
    public void hideProgressbar() {
        hideMifosProgressDialog();
    }

    @Override
    public void showError() {
        etComment.setEnabled(true);
        Toaster.show(rootView, getString(R.string.error_updating_status));
    }

    @Override
    public void onStart() {
        super.onStart();
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideMifosProgressDialog();
        tasksBottomSheetPresenter.detachView();
    }
}