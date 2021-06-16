package com.example.tittle_tattle.ui.homeScreen.fragments.publish;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.algorithm.ISUser;
import com.example.tittle_tattle.data.AppDatabase;
import com.example.tittle_tattle.data.models.Message;
import com.example.tittle_tattle.data.models.Subscription;
import com.example.tittle_tattle.databinding.FragmentDashboardBinding;
import com.example.tittle_tattle.ui.homeScreen.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private FragmentDashboardBinding binding;

    private final int MAX_CHECKABLE = 3;
    private final ArrayList<Integer> topics = new ArrayList<>(MAX_CHECKABLE);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sharedViewModel =
                new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<Subscription> subscriptionList = ISUser.getUser().getSubscriptions();

        if (subscriptionList.size() == 0) {
            root.findViewById(R.id.textViewNoTopics1).setVisibility(View.VISIBLE);
            root.findViewById(R.id.textViewNoTopics2).setVisibility(View.VISIBLE);
            root.findViewById(R.id.imageViewNoTopics).setVisibility(View.VISIBLE);

            root.findViewById(R.id.editText2).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.list_view).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.publish_btn).setVisibility(View.INVISIBLE);
        } else {
            root.findViewById(R.id.textViewNoTopics1).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.textViewNoTopics2).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.imageViewNoTopics).setVisibility(View.INVISIBLE);

            root.findViewById(R.id.editText2).setVisibility(View.VISIBLE);
            root.findViewById(R.id.list_view).setVisibility(View.VISIBLE);
            root.findViewById(R.id.publish_btn).setVisibility(View.VISIBLE);

            ListView listView = root.findViewById(R.id.list_view);

            SubscriptionAdapter adapter = new SubscriptionAdapter(requireContext(), subscriptionList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                private int selected = 0;

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    CheckedTextView checkedTextView = (CheckedTextView) view;

                    if (checkedTextView.isChecked()) {
                        if (selected < MAX_CHECKABLE) {
                            if (topics.size() <= selected) {
                                topics.add(((Subscription)adapterView.getItemAtPosition(i)).getSubscription_id());
                            } else {
                                topics.set(selected, ((Subscription)adapterView.getItemAtPosition(i)).getSubscription_id());
                            }
                            selected++;
                        } else {
                            listView.setItemChecked(i, false);
                            Toast.makeText(getContext(), "You can't select more than 3 items!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        selected--;
                        topics.set(selected, null);

                    }
                }
            });

            Button button = root.findViewById(R.id.publish_btn);
            button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            button.setOnClickListener(view -> {
                if (topics.isEmpty()) {
                    Toast.makeText(getContext(), "At least one topic must be selected!", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText editText = root.findViewById(R.id.editText2);
                String content = editText.getText().toString();

                if (content.isEmpty()) {
                    Toast.makeText(getContext(), "Content can't be empty!", Toast.LENGTH_LONG).show();
                    return;
                }

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("[DB]", "publish message");

                        AppDatabase.getInstance(view.getContext()).publishMessage(
                                new Message(
                                        ISUser.getUser().getId(),
                                        content,
                                        topics.get(0),
                                        1 < topics.size() ? topics.get(1) : null,
                                        2 < topics.size() ? topics.get(2): null)
                        );
                    }
                });

                editText.setText("");
                listView.clearChoices();

                Navigation.findNavController(view).navigate(R.id.action_navigation_dashboard_to_navigation_home);
            });
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}