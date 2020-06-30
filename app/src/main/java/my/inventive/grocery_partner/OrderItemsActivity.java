package my.inventive.grocery_partner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import my.inventive.grocery_partner.adapters.OrderItemsAdapter;
import my.inventive.grocery_partner.modules.Item;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderItemsActivity extends AppCompatActivity
{
    TextView orderId;
    RecyclerView rv;
    LinearLayoutManager manager;
    RecyclerView.Adapter<OrderItemsAdapter.OrderItemsAdapterViewHolder>adapter;
    ArrayList<Item>al;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_items_list);
        rv=findViewById(R.id.allItemsRv);
        al=new ArrayList<>();
        manager=new LinearLayoutManager(this);
        orderId=findViewById(R.id.orderId);
        final String id= getIntent().getStringExtra("orderId");
        orderId.setText("Order Id : "+id);

        final ProgressDialog pd = new ProgressDialog(OrderItemsActivity.this);
        pd.setTitle("Pet Seller Seller");
        pd.setMessage("Please Wait...");
        pd.show();
        String url = "https://inventivepartner.com/petmart/orderItemsList.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                pd.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String sucess = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (sucess.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            al.add(new Item(object.getString("item_id"),object.getString("item_name"),object.getString("item_price"),object.getString("item_quantity"),object.getString("order_id")));
                        }
                        adapter=new OrderItemsAdapter(OrderItemsActivity.this,al);
                        rv.setLayoutManager(manager);
                        rv.setAdapter(adapter);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(OrderItemsActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("orderId",id);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OrderItemsActivity.this, new HurlStack());
        requestQueue.add(stringRequest);
    }
}
