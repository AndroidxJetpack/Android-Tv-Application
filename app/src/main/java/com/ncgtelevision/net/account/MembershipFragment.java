package com.ncgtelevision.net.account;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.leanback.app.BrowseSupportFragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ncgtelevision.net.R;
import com.ncgtelevision.net.account.model.AccountModel;
import com.ncgtelevision.net.account.model.Datum;
import com.ncgtelevision.net.account.model.DeleteChannelRequest;
import com.ncgtelevision.net.account.model.DeleteChannelResponse;
import com.ncgtelevision.net.account.model.Membership;
import com.ncgtelevision.net.interfaces.NavigationMenuCallback;
import com.ncgtelevision.net.retrofit_clients.ApiClient;
import com.ncgtelevision.net.retrofit_clients.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ncgtelevision.net.utilities.CommonUtility.custom_loader;
import static com.ncgtelevision.net.utilities.CommonUtility.isEmailValid;
import static com.ncgtelevision.net.utilities.CommonUtility.isStringEmpty;
import static com.ncgtelevision.net.utilities.CommonUtility.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MembershipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MembershipFragment extends Fragment implements Callback<AccountModel> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "MembershipFragment";
    Context context;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mMembershipTV;
    private TextView mAccountTV;
    private LinearLayout mMembershipLL;
    private LinearLayout mAccountLL;
    private NavigationMenuCallback navigationMenuCallback;
    private TextView mTitleTV;
    private EditText mUserNameED;
    private EditText mFirstNameED;
    private EditText mLastNameED;
    private EditText mEmailED;
    private EditText mConfPassED;
    private EditText mPasswordED;
    private Button mUpdateButton;
    private AccountModel mAccountModel;
    private ProgressDialog mDialog;
    private View.OnKeyListener pressKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.d(ARG_PARAM2, "onKey: "+ event.toString());
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (KeyEvent.KEYCODE_DPAD_CENTER == keyCode || KeyEvent.KEYCODE_ENTER == keyCode || KeyEvent.KEYCODE_DPAD_CENTER == keyCode) {
                    if(v.getId() == R.id.account){
                        mMembershipLL.setVisibility(View.GONE);
                        mAccountLL.setVisibility(View.VISIBLE);
                        mTitleTV.setText(getContext().getString(R.string.my_account));
                        return true;
                    }

                    if(v.getId() == R.id.membership){
                        mAccountLL.setVisibility(View.GONE);
                        mMembershipLL.setVisibility(View.VISIBLE);
                        mTitleTV.setText(getContext().getString(R.string.my_membership));
                        return true;
                    }
//                    Toast.makeText(context, "Enter Pressed", Toast.LENGTH_LONG).show();
//                    return false;
                } else if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
                    if(v.getId() == R.id.account){
                        mMembershipTV.requestFocus();
                        return true;
                    }

                    if(v.getId() == R.id.membership){
                        navigationMenuCallback.navMenuToggle(true);
                    }
//                    Toast.makeText(context, "Left Pressed", Toast.LENGTH_LONG).show();
                    return false;
                } else if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
                    if(v.getId() == R.id.account){
                        v.requestFocus(View.FOCUS_RIGHT);
                        return true;
                    }

                    if(v.getId() == R.id.membership){
                        mAccountTV.requestFocus();
                        return true;
                    }
//                Toast.makeText(context, "Right Pressed", Toast.LENGTH_LONG).show();
                    return false;
                }  else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {
                    if(v.getId() == R.id.account || v.getId() == R.id.membership){
                        if(mAccountLL.getVisibility() == View.VISIBLE) {
                            mAccountLL.getChildAt(1).requestFocus();
                        }else  if (mMembershipLL.getChildAt(1) != null){
                            mMembershipLL.getChildAt(1).requestFocus();;
                        }
                        return true;
                    }
//                Toast.makeText(context, "Down Pressed", Toast.LENGTH_LONG).show();
                    return false;
                }

            }
            return false;
        }
    };
    public MembershipFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MembershipFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MembershipFragment newInstance(String param1, String param2) {
        MembershipFragment fragment = new MembershipFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_membership, container, false);
        initilizeViews(view);
        mDialog = custom_loader(getActivity());
        context = getActivity();
//        callAPI();
        callMemberShipAPI(true);
//        view.getRootView().setOnKeyListener(pressKeyListener);
        return view;
    }

    private void callMemberShipAPI(final boolean isCallAccount) {
        mDialog.show();
        ApiClient.getClient(context).create(ApiInterface.class)
                .getMembership().enqueue(new Callback<AccountModel>() {
            @Override
            public void onResponse(Call<AccountModel> call, Response<AccountModel> response) {
                mDialog.dismiss();
                if(isCallAccount)
                      callAPI();
                if(response.body() != null){
                    AccountModel accountModel = response.body();
                    if(accountModel.getMemberships()!=null && accountModel.getMemberships().size() > 0){
                        initilizeMembeship(accountModel.getMemberships());
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountModel> call, Throwable t) {
                t.printStackTrace();
                mDialog.dismiss();
                if(isCallAccount)
                   callAPI();
            }
        });
    }

    private void initilizeMembeship(List<Membership> memberships) {
        mMembershipLL.removeAllViews();
        View header = LayoutInflater.from(context).inflate(R.layout.membership_item_header, mMembershipLL, false);
        mMembershipLL.addView(header);
        for (int i=0; i<memberships.size(); i++){
            final int index = i;
            final View memberItemView = LayoutInflater.from(context).inflate(R.layout.membership_item, mMembershipLL, false);
            TextView memberShipView = (TextView) memberItemView.findViewById(R.id.membership);
            TextView price = (TextView) memberItemView.findViewById(R.id.price);
            TextView status = (TextView) memberItemView.findViewById(R.id.status);
            TextView nextPayment = (TextView) memberItemView.findViewById(R.id.nextPayment);
            Button manage = (Button) memberItemView.findViewById(R.id.manage);
            manage.setPaintFlags(manage.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            Membership membership = memberships.get(i);

            memberShipView.setText(membership.getMembership());
            price.setText(membership.getPrice());
            status.setText(membership.getStatus());
            nextPayment.setText(membership.getNextPayment());

            manage.setOnKeyListener((view, i1, keyEvent) -> {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if(i1 == KeyEvent.KEYCODE_DPAD_DOWN && memberships.size()-1 == index){
                        view.requestFocus();
                        return true;
                    }
                    if (KeyEvent.KEYCODE_DPAD_CENTER == i1 || KeyEvent.KEYCODE_ENTER == i1 || KeyEvent.KEYCODE_DPAD_CENTER == i1) {
                        deleteMemberShip(memberships.get(index));
                        return true;
                    }
                }
                return false;
            });
            manage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b){
                        memberItemView.setBackgroundColor(getResources().getColor(R.color.background_grey));
                    }else{
                        memberItemView.setBackgroundColor(getResources().getColor(R.color.background_theme));
                    }
                }
            });

            mMembershipLL.addView(memberItemView);
        }
    }

    private void deleteMemberShip(Membership membership) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        //set title for alert dialog
        builder.setTitle("Alert!!!");
        //set message for alert dialog
        builder.setMessage("Do you want to delete the membership?");
        builder.setIcon(R.drawable.splash_logo);


        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteChannelRequest req = new DeleteChannelRequest();
                req.setPlan(membership.getPlan());
                ApiClient.getClient(context).create(ApiInterface.class).deleteChannel(req).enqueue(new Callback<DeleteChannelResponse>() {
                    @Override
                    public void onResponse(Call<DeleteChannelResponse> call, Response<DeleteChannelResponse> response) {
                        //  mDialog.dismiss();
                        if(response.body() != null){
                            if(response.body().getStatus()){
                                showToast(context, getString(R.string.membership_unsubscribe));
                                callMemberShipAPI(false);
                            }else{
                                showToast(context,response.body().getMessage());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<DeleteChannelResponse> call, Throwable t) {
                        t.printStackTrace();
                        mDialog.dismiss();
                    }
                });

                dialog.dismiss();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
// Create the AlertDialog
        AlertDialog alertDialog  = builder.create();
        // Set other dialog properties
        alertDialog.setCancelable(false);
        alertDialog.show();

      //  mDialog.show();

    }

    private void callAPI() {
        mDialog.show();
        ApiClient.getClient(context).create(ApiInterface.class)
                .getAccount().enqueue(this);
    }

    private void initilizeViews(View view) {
        mTitleTV = (TextView) view.findViewById(R.id.title_tv);
        mMembershipTV = (TextView) view.findViewById(R.id.membership);
        mAccountTV = (TextView) view.findViewById(R.id.account);
        mMembershipLL = (LinearLayout) view.findViewById(R.id.membership_container);
        mAccountLL = (LinearLayout) view.findViewById(R.id.account_container);
        mMembershipTV.setOnKeyListener(pressKeyListener);
        mAccountTV.setOnKeyListener(pressKeyListener);
//        Account Views
        mUserNameED = (EditText) view.findViewById(R.id.userName);
        mFirstNameED = (EditText) view.findViewById(R.id.firstName);
        mLastNameED = (EditText) view.findViewById(R.id.lastName);
        mEmailED = (EditText) view.findViewById(R.id.ed_email);
        mPasswordED = (EditText) view.findViewById(R.id.ed_password);
        mConfPassED = (EditText) view.findViewById(R.id.ed_conf_password);
        mUpdateButton = (Button) view.findViewById(R.id.btn_sign_in);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccount();
            }
        });
        intializeFirstFocus();
//        mMembershipTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mMembershipLL.setVisibility(View.VISIBLE);
//                mAccountLL.setVisibility(View.GONE);
//            }
//        });
    }

    ///Account Update
    private void updateAccount() {
        String userName = mUserNameED.getText().toString();
        String firstName = mFirstNameED.getText().toString();
        String lastName = mLastNameED.getText().toString();
        String email = mEmailED.getText().toString();
        String confPassword = mConfPassED.getText().toString();
        String password = mPasswordED.getText().toString();

        if(isStringEmpty(userName)){
            showToast(context,"UserName is not empty");
            return;
        }
        if(isStringEmpty(firstName)){
            showToast(context,"First Name is not empty!!!");
            return;
        }
        if(isStringEmpty(lastName)){
            showToast(context,"Last Name is not empty!!!");
            return;
        }
        if(!isEmailValid(email)){
            showToast(context,"Please enter Valid!!!");
            return;
        }
        mDialog.show();
        Datum data = new Datum();
        data.setUsername(userName);
        data.setNewFirstName(firstName);
        data.setNewLastName(lastName);
        data.setNewUserEmail(email);
        data.setNewUserPassword(password);
        data.setConfirmUserPassword(confPassword);

        ApiClient.getClient(context).create(ApiInterface.class).updateAccount(data).enqueue(new Callback<AccountModel>() {
            @Override
            public void onResponse(Call<AccountModel> call, Response<AccountModel> response) {
                mDialog.dismiss();
                if(response.body() != null){
                    AccountModel accountModel = response.body();
                    showToast(context, accountModel.getMessage());
                    if(accountModel.getStatus()){
                        Datum datum = mAccountModel.getData().get(0);
                        datum.setNewFirstName(data.getNewFirstName());
                        datum.setNewLastName(data.getNewLastName());
                        datum.setNewUserEmail(data.getNewUserEmail());
                        setAccount();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountModel> call, Throwable t) {
                t.printStackTrace();
                mDialog.dismiss();
            }
        });
//        if(isStringEmpty(password)){
//            showToast(context,"UserName is not empty");
//            return;
//        }
//        if(isStringEmpty(confPassword)){
//            showToast(context,"UserName is not empty");
//            return;
//        }


    }

    // Intitialize Account
    private void setAccount() {
        if(mUserNameED != null && mAccountModel != null &&  mAccountModel.getData() != null &&  mAccountModel.getData().size() != 0){
            Datum data = mAccountModel.getData().get(0);
            mUserNameED.setText(data.getUsername());
            mFirstNameED.setText(data.getNewFirstName());
            mLastNameED.setText(data.getNewLastName());
            mPasswordED.setText(data.getNewUserPassword());
            mConfPassED.setText(data.getConfirmUserPassword());
            mEmailED.setText(data.getNewUserEmail());
        }
    }

    public void setNavigationMenuCallback(NavigationMenuCallback navigationMenuCallback) {
        this.navigationMenuCallback = navigationMenuCallback;
    }

    @Override
    public void onResponse(Call<AccountModel> call, Response<AccountModel> response) {
        mDialog.dismiss();
        if(response.body() != null){
            mAccountModel = response.body();
            setAccount();
        }
    }

    @Override
    public void onFailure(Call<AccountModel> call, Throwable t) {
       t.printStackTrace();
       mDialog.dismiss();
    }
    public void intializeFirstFocus(){
        if(mMembershipTV != null)
            mMembershipTV.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMembershipTV.requestFocus();
                }
            }, 10);
    }

}

