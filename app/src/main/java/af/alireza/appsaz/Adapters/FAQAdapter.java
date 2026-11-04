package af.alireza.appsaz.Adapters;

import af.alireza.appsaz.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;
import java.util.ArrayList;
import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {

    private final List<FAQItem> faqItems;

    public FAQAdapter(List<FAQItem> faqItems) {
        this.faqItems = faqItems;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQItem faqItem = faqItems.get(position);
        holder.questionTextView.setText(faqItem.getQuestion());
        holder.answerTextView.setText(faqItem.getAnswer());

        // Toggle visibility on question click
        holder.questionTextView.setOnClickListener(v -> {
            boolean isVisible = holder.answerTextView.getVisibility() == View.VISIBLE;
            TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView, new AutoTransition());
            holder.answerTextView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return faqItems.size();
    }

    static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView, answerTextView;

        FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            answerTextView = itemView.findViewById(R.id.answerTextView);
        }
    }
    public static List<FAQItem> getFAQItems(Context context) {
        List<FAQItem> faqItems = new ArrayList<>();

        // Load questions and answers from string arrays
        String[] questions = context.getResources().getStringArray(R.array.faq_questions);
        String[] answers = context.getResources().getStringArray(R.array.faq_answers);

        // Match questions with answers and create FAQItem list
        for (int i = 0; i < questions.length; i++) {
            faqItems.add(new FAQItem(questions[i], answers[i]));
        }

        return faqItems;
    }
}