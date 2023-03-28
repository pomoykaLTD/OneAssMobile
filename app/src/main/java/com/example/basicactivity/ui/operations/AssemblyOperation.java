package com.example.basicactivity.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.basicactivity.databinding.AssemblyOperationBinding;
import com.example.basicactivity.tiles.Item;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AssemblyOperation extends Fragment implements MyFrame {
    private AssemblyOperationBinding binding;
    private String operationId;
    private String operationName;

    private String productId;

    public static final String REF="AssemblyOperation.Reference";
    public static final String BARCODE="BARCODE.Contents";
    public static final String refProduct="refProd";

    public static final String refSpecification="refSpec";
    public static final String refMaterial="refMat";
    private ArrayAdapter<Item> adapter;

    private final List<Item> items = new ArrayList<>();

    public AssemblyOperation setContextData( String task, String strJSONdata ){
        //this.task=task;
            try {
                JSONObject jsonObject = new JSONObject(strJSONdata);
                operationId=jsonObject.getString("id");
                operationName=jsonObject.getString("name");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        return this;
    }

    @Override
    public String getTitle() { return operationName; }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AssemblyOperationBinding.inflate(inflater, container, false);

        MyPagerAdapter adapter = new MyPagerAdapter(this);
        adapter.addFragment(new FirstFragment());
        adapter.addFragment(new SecondFragment());
        binding.viewPager.setAdapter(adapter);

        //кнопка добавления продукции
        binding.btSetProduct.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(REF, refProduct);
            Intent intent = new Intent(getActivity(), BarcodeScanner.class);
            intent.putExtras(bundle);
            launcher.launch(intent);
        });

        //кнопка выбора спецификации
        binding.btSetSpecification.setOnClickListener(view -> {
//                Bundle bundle = new Bundle();
//                bundle.putString(REF, refSpecification);
//                Intent intent = new Intent(getActivity(), BarcodeScanner.class);
//                intent.putExtras(bundle);
//                launcher.launch(intent);
        });

        //кнопка добавления материалов в список
        binding.btAddMaterials.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(REF, refMaterial);
            Intent intent = new Intent(getActivity(), BarcodeScanner.class);
            intent.putExtras(bundle);
            launcher.launch(intent);
        });

        //кнопка запись операции, отправляем команду на сервер
        binding.btFinish.setOnClickListener(view -> {

            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            //задачей селектора - выбор операции, выполняем передачу данных на сервер
//                new ConnTask( getActivity(), Init.saveOperation, getInstance()){
//                    @Override
//                    public void onPostExecute() {
//
//                        if(this.status){
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString("JSON",this.result);
//                            bundle.putString("task",Init.getOperations);
//                            //bundle.putParcelable("assemblyOperation", assemblyOperation);
//                            NavController navController = Navigation.findNavController( binding.btSetProduct );
//                            navController.navigate(R.id.fr_Selector, bundle);
//
//                        }
//                        else Toast.makeText(getContext(),R.string.badLogIn, Toast.LENGTH_SHORT).show();
//                    }
//                }.execute();
        });

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        binding.ListMaterials.setAdapter(adapter);

        return binding.getRoot();
    }

    //получить результат работы сканера
    ActivityResultLauncher<Intent> launcher = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                String resultValue = data.getStringExtra("RESULT");
                //String resultValue = data.getStringExtra(BARCODE);
                String ref=data.getStringExtra(REF);

                if(ref.equals(refProduct)){
                    productId=resultValue;
                    binding.btSetProduct.setText(resultValue);
                }else if(ref.equals(refMaterial)){
                    adapter.add(new Item(resultValue,resultValue));
                }
            }
    });

    public class MyPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        public MyPagerAdapter(FragmentActivity activity) {
            super(activity);
        }
        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }
        @Override
        public Fragment createFragment(int position) {
            return mFragments.get(position);
        }
        @Override
        public int getItemCount() {
            return mFragments.size();
        }
    }

    //возвращает объектную можедель JSON операции Сборка
    private String getInstance(){
        JSONObject json = new JSONObject();
        try {
            json.put("id",operationId);
            json.put("productId",productId);
            json.put("items",items);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json.toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}