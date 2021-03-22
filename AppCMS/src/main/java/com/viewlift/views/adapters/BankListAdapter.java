package com.viewlift.views.adapters;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class BankListAdapter extends RecyclerView.Adapter<BankListAdapter.CustomViewHolder> {

    private AppCMSPresenter appCMSPresenter;
    private ArrayList<NetBankingItem> bankList;

    public BankListAdapter(AppCMSPresenter appCMSPresenter) {
        this.appCMSPresenter = appCMSPresenter;
        bankList = getNetBankingArray();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_list_adapter, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        NetBankingItem item = bankList.get(position);
        holder.bankName.setText(item.getValue());
        holder.itemView.setOnClickListener(v -> {
            if (appCMSPresenter.getJusPayUtils() != null && appCMSPresenter.getCurrentActivity() != null)
                appCMSPresenter.getJusPayUtils().urlBasedPayments(appCMSPresenter.getCurrentActivity().getString(R.string.nb), item.getKey());
        });
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    private ArrayList<NetBankingItem> getNetBankingArray() {
        HashMap<String, String> map = getNetBankList();
        ArrayList<NetBankingItem> items = new ArrayList<>();
        String[] bankCodes = new String[]{
                "NB_BOI","NB_BOM","NB_CBI","NB_CORP","NB_DCB","NB_FED","NB_INDB","NB_INDUS","NB_IOB","NB_JNK","NB_KARN","NB_KVB","NB_SBBJ",
                "NB_SBH","NB_SBM","NB_SBT","NB_SOIB","NB_UBI","NB_UNIB","NB_VJYB", "NB_YESB","NB_CUB","NB_CANR","NB_SBP","NB_DEUT","NB_KOTAK","NB_DLS",
                "NB_ING","NB_PNBCORP","NB_PNB","NB_BOB","NB_CSB","NB_OBC","NB_SCB","NB_TMB", "NB_SARASB","NB_SYNB","NB_UCOB","NB_BOBCORP","NB_ALLB","NB_BBKM",
                "NB_JSB","NB_LVBCORP", "NB_LVB","NB_NKGSB","NB_PMCB","NB_PNJSB","NB_RATN","NB_ANDHRA",
                "NB_RBS","NB_SVCB","NB_TNSC","NB_DENA", "NB_COSMOS","NB_DBS","NB_IDFC"
        };

        for (String bankCode : bankCodes) {
            String bank = map.get(bankCode);
            if (!TextUtils.isEmpty(bank)) {
                NetBankingItem item = new NetBankingItem(bankCode, bank);
                items.add(item);
            }
        }
        Collections.sort(items, (o1, o2) -> o1.getValue().compareToIgnoreCase(o2.getValue()));
        return items;
    }

    private HashMap<String, String> getNetBankList(){
        HashMap<String, String> map = new HashMap<>();
        map.put("NB_BOI", "Bank of India");
        map.put("NB_BOM", "Bank of Maharashtra");
        map.put("NB_CBI", "Central Bank Of India");
        map.put("NB_CORP", "Corporation Bank");
        map.put("NB_DCB", "Development Credit Bank");
        map.put("NB_FED", "Federal Bank");
        map.put("NB_INDB", "Indian Bank");
        map.put("NB_INDUS", "IndusInd Bank");
        map.put("NB_IOB", "Indian Overseas Bank");
        map.put("NB_JNK", "Jammu and Kashmir Bank");
        map.put("NB_KARN", "Karnataka Bank");
        map.put("NB_KVB", "Karur Vysya");
        map.put("NB_SBBJ", "State Bank of Bikaner and Jaipur");
        map.put("NB_SBH", "State Bank of Hyderabad");
        map.put("NB_SBM", "State Bank of Mysore");
        map.put("NB_SBT", "State Bank of Travancore");
        map.put("NB_SOIB", "South Indian Bank");
        map.put("NB_UBI", "Union Bank of India");
        map.put("NB_UNIB", "United Bank Of India");
        map.put("NB_VJYB", "Vijaya Bank");
        map.put("NB_YESB", "Yes Bank");
        map.put("NB_CUB", "CityUnion");
        map.put("NB_CANR", "Canara Bank");
        map.put("NB_SBP", "State Bank of Patiala");
        map.put("NB_DEUT", "Deutsche Bank");
        map.put("NB_KOTAK", "Kotak Bank");
        map.put("NB_DLS", "Dhanalaxmi Bank");
        map.put("NB_ING", "ING Vysya Bank");
        map.put("NB_ANDHRA", "Andhra Bank");
        map.put("NB_PNBCORP", "Punjab National Bank CORPORATE");
        map.put("NB_PNB", "Punjab National Bank Retail");
        map.put("NB_BOB", "Bank of Baroda");
        map.put("NB_CSB", "Catholic Syrian Bank");
        map.put("NB_OBC", "Oriental Bank Of Commerce");
        map.put("NB_SCB", "Standard Chartered Bank");
        map.put("NB_TMB", "Tamilnad Mercantile Bank");
        map.put("NB_SARASB", "Saraswat Bank");
        map.put("NB_SYNB", "Syndicate Bank");
        map.put("NB_UCOB", "UCO Bank");
        map.put("NB_BOBCORP", "Bank of Baroda Corporate");
        map.put("NB_ALLB", "Allahabad Bank");
        map.put("NB_BBKM", "Bank of Bahrain and Kuwait");
        map.put("NB_JSB", "Janata Sahakari Bank");
        map.put("NB_LVBCORP", "Lakshmi Vilas Bank Corporate");
        map.put("NB_LVB", "Lakshmi Vilas Bank Retail");
        map.put("NB_NKGSB", "North Kanara GSB");
        map.put("NB_PMCB", "Punjab and Maharashtra Coop Bank");
        map.put("NB_PNJSB", "Punjab and Sind Bank");
        map.put("NB_RATN", "Ratnakar Bank");
        map.put("NB_RBS", "Royal Bank of Scotland");
        map.put("NB_SVCB", "Shamrao Vithal Coop Bank");
        map.put("NB_TNSC", "Tamil Nadu State Apex Coop Bank");
        map.put("NB_DENA", "DENA Bank");
        map.put("NB_COSMOS", "COSMOS Bank");
        map.put("NB_DBS", "DBS Bank Ltd");
        map.put("NB_IDFC", "IDFC Bank");
        return map;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView bankName;

        private CustomViewHolder(View itemView) {
            super(itemView);
            bankName = itemView.findViewById(R.id.bankName);
            bankName.setTextColor(Color.parseColor("#535665"));
        }
    }

    class NetBankingItem {

        private String key;
        private String value;

        NetBankingItem(String ketStr, String valueStr) {
            this.key = ketStr;
            this.value = valueStr;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}