#include<iostream>
#include <bits/stdc++.h>
#include<vector>
#include<cctype>
#include<math.h>
#include<cmath>
#include<string>
#include<algorithm>
using namespace std;
class Graph
{
    int V;    // No. of vertices
    list<int> *adj;    // Pointer to an array containing adjacency lists
    bool isCyclicUtil(int v, bool visited[], bool *rs);  // used by isCyclic()
public:
    Graph(int V);   // Constructor
    void addEdge(int v, int w);   // to add an edge to graph
    bool isCyclic();    // returns true if there is a cycle in this graph
};

Graph::Graph(int V)
{
    this->V = V;
    adj = new list<int>[V];
}

void Graph::addEdge(int v, int w)
{
    adj[v].push_back(w); // Add w to v’s list.
}

// This function is a variation of DFSUtil() in
// https://www.geeksforgeeks.org/archives/18212
bool Graph::isCyclicUtil(int v, bool visited[], bool *recStack)
{
    if(visited[v] == false)
    {
        // Mark the current node as visited and part of recursion stack
        visited[v] = true;
        recStack[v] = true;

        // Recur for all the vertices adjacent to this vertex
        list<int>::iterator i;
        for(i = adj[v].begin(); i != adj[v].end(); ++i)
        {
            if ( !visited[*i] && isCyclicUtil(*i, visited, recStack) )
                return true;
            else if (recStack[*i])
                return true;
        }

    }
    recStack[v] = false;  // remove the vertex from recursion stack
    return false;
}

// Returns true if the graph contains a cycle, else false.
// This function is a variation of DFS() in
// https://www.geeksforgeeks.org/archives/18212
bool Graph::isCyclic()
{
    // Mark all the vertices as not visited and not part of recursion
    // stack
    bool *visited = new bool[V];
    bool *recStack = new bool[V];
    for(int i = 0; i < V; i++)
    {
        visited[i] = false;
        recStack[i] = false;
    }

    // Call the recursive helper function to detect cycle in different
    // DFS trees
    for(int i = 0; i < V; i++)
        if ( !visited[i] && isCyclicUtil(i, visited, recStack))
            return true;

    return false;
}

int main()
{
    vector<char> read_or_write;
    vector<int> tran_no;
    vector<char> variable;
    vector<char> uni_variable_vec;
    set<char>uni_var;
    vector<int> pre_vertex;
    vector<int> post_vertex;

    int no_of_transaction, no_of_variable;

    cout<<"How many transaction: ";
    cin>>no_of_transaction;

    int i,no_edge=0;
    char rOw,var,now_var,now_rOw,look_for_rOw;
    int tranNo;

    for(i=0;i<no_of_transaction;i++)
    {
        cin>>rOw;
        cin>>tranNo;
        cin>>var;

        read_or_write.push_back(rOw);
        tran_no.push_back(tranNo);
        variable.push_back(var);
        uni_var.insert(var);
    }

    for(auto iti: uni_var)
        uni_variable_vec.push_back(iti);

    now_var=variable[0];
    now_rOw=read_or_write[0];
    for(i=0;i<no_of_transaction;i++)
    {
        cout<<read_or_write[i]<<tran_no[i]<<"("<<variable[i]<<")"<<endl;
    }

    for(int j=0;j<uni_variable_vec.size();j++)
    {
        for(i=0;i<no_of_transaction;i++)
        {
            if((read_or_write[i]=='r')&&(variable[i]==uni_variable_vec[j]))
            {
                for(int k=i+i;k<no_of_transaction;k++)
                {
                    if((read_or_write[k]=='w')&&(variable[k]==uni_variable_vec[j]))
                    {
                        pre_vertex.push_back(tran_no[i]-1);
                        post_vertex.push_back(tran_no[k]-1);
                        no_edge++;
                    }
                }
            }

            if((read_or_write[i]=='w')&&(variable[i]==uni_variable_vec[j]))
            {
                for(int k=i+i;k<no_of_transaction;k++)
                {
                    if(variable[k]==uni_variable_vec[j])
                    {
                        pre_vertex.push_back(tran_no[i]-1);
                        post_vertex.push_back(tran_no[k]-1);
                        no_edge++;
                    }
                }
            }
        }
    }

    Graph g(tran_no.size());
    for(i=0;i<no_edge;i++)
    {

        if(pre_vertex[i]!=post_vertex[i])
        {
            g.addEdge(pre_vertex[i], post_vertex[i]);
            cout<<pre_vertex[i]<<","<<post_vertex[i]<<endl;
        }

    }

    if(g.isCyclic())
        cout << "Graph contains cycle";
    else
        cout << "Graph doesn't contain cycle";
}
