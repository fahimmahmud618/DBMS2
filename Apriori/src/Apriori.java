import java.util.*;

public class Apriori
{
    public List<Set<String>> apriori(List<Set<String>> dataset, int minSupport)
    {

        List<String> unique_items = new ArrayList<>();
        for (Set<String> transaction : dataset)
        {
            for (String each_transaction : transaction)
            {
                if (!unique_items.contains(each_transaction))
                {
                    unique_items.add(each_transaction);
                }
            }
        }

        Map<Set<String>, Integer> candidates = new HashMap<>();
        for (String each_unique_item : unique_items)
        {
            Set<String> items_in_UniqueItemSet = Set.of(each_unique_item);
            int support = 0;
            for (Set<String> transaction : dataset)
            {
                if (transaction.contains(each_unique_item))
                {
                    support++;
                }
            }
            if (support >= minSupport)
            {
                candidates.put(items_in_UniqueItemSet, support);
            }
        }

        System.out.println(candidates);
        List<Set<String>> frequentItemSets = new ArrayList<>(candidates.keySet());


        while (!candidates.isEmpty())
        {

            Map<Set<String>, Integer> nextCandidates = new HashMap<>();
            for (Set<String> itemSet1 : candidates.keySet())
            {
                for (Set<String> itemSet2 : candidates.keySet())
                {
                    Set<String> unionSet = new HashSet<>(itemSet1);
                    unionSet.addAll(itemSet2);
                    if (unionSet.size() == itemSet1.size() + 1)
                    {
                        nextCandidates.put(unionSet, 0);
                    }
                }
            }

            candidates = new HashMap<>();
            for (Set<String> itemSet : nextCandidates.keySet())
            {
                int support = 0;
                for (Set<String> transaction : dataset)
                {
                    if (transaction.containsAll(itemSet))
                    {
                        support++;
                    }
                }
                if (support >= minSupport)
                {
                    candidates.put(itemSet, support);
                }
            }
            frequentItemSets.addAll(candidates.keySet());
            System.out.println(candidates);
        }

        return frequentItemSets;
    }
}
