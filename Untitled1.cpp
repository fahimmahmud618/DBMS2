#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>
#include <numeric>
#include <cmath>
using namespace std;

void initialize_new_vector(int turn);
double mean(const vector<double>& numbers);
double variance(const vector<double>& numbers);
double p_classifiar(vector<double> numbers);
void train(int turn);
double test(int turn);
double kfold(int turn);
double p_minTemp_yes(double input);
double p_maxTemp_yes(double input);
double p_humidity_yes(double input);
double p_gauss(double mintemp,double maxtemp, double humid);

vector<double> minTemp,maxTemp,humidity;
vector<int> cloud9am, cloud3pm, raintoday, raintomorrow;

vector<double> newminTemp,newmaxTemp,newhumidity;
vector<int> newcloud9am, newcloud3pm, newraintoday, newraintomorrow;

double minTemp_mean,minTemp_vari,maxTemp_mean,maxTemp_vari,humidity_mean,humidity_vari,cloud9am_yes,cloud3pm_yes,raintoday_yes,raintomorrow_yes;
double classifiar_prob;


void initialize_new_vector(int turn)
{
    newcloud3pm.clear();
    newcloud9am.clear();
    newhumidity.clear();
    newmaxTemp.clear();
    newminTemp.clear();
    newraintoday.clear();
    newraintomorrow.clear();

   // cout<<"clear done"<<endl;
    int start=turn*100;
    int endd=start+99;
    int j=0;
    for(int i=0;i<1000;i++)
    {
        if(i>=start && i<=endd)
            continue;
        else if(raintomorrow[i]==1)
        {
           // cout<<"hi";
           /* newminTemp[j]=minTemp[i];
            newmaxTemp[j]=maxTemp[i];
            newhumidity[j]=humidity[i];
            newcloud9am[j]=cloud9am[i];
            newcloud3pm[j]=cloud3pm[i];
            newraintoday[j]=raintoday[i];
            newraintomorrow[j]=raintomorrow[i];*/

            newminTemp.push_back(minTemp[i]);
            newmaxTemp.push_back(maxTemp[i]);
            newhumidity.push_back(humidity[i]);
           // cout<<"taken ";
            j++;
        }
    }

}
double mean(const vector<double>& numbers) {
    double sum = accumulate(numbers.begin(), numbers.end(), 0.0);
    return sum / numbers.size();
}

double variance(const vector<double>& numbers) {
    double mean_value = mean(numbers);
    double sum_of_squared_differences = 0.0;
    for (double number : numbers) {
        sum_of_squared_differences += pow(number - mean_value, 2);
    }
    double variance = sum_of_squared_differences / numbers.size();
    return variance;
}

double p_classifiar()
{
    double yes=0,c9am_yes=0,c3pm_yes=0,rtoday_yes=0;

    for(int i=0;i<1000;i++)
    {
        if(raintomorrow[i]==1)
            yes++;
        if(cloud3pm[i]==1 && raintomorrow[i]==1)
            c3pm_yes++;
        else if(cloud9am[i]==1 && raintomorrow[i]==1)
            c9am_yes++;
        else if(raintoday[i]==1 && raintomorrow[i]==1)
            rtoday_yes++;
    }
   // cout<<yes<<c9am_yes<<c3pm_yes<<rtoday_yes;
    double ret = ((c3pm_yes/yes)*(c9am_yes/yes)*(rtoday_yes/yes)*(yes/1000));
   // cout<<endl<<ret<<endl;
    //total_yes=yes;
    return ret;
}

void train(int turn)
{
    minTemp_mean=mean(newminTemp);
    minTemp_vari=variance(newminTemp);
    maxTemp_mean=mean(newmaxTemp);
    maxTemp_vari=variance(newmaxTemp);
    humidity_mean=mean(newhumidity);
    humidity_vari=variance(newhumidity);

    //cout<<"initialize done"<<endl;
}

double test(int turn)
{
    int start=turn*100;
    int endd=start+99;

    int accuracy_cnt=0;
    double posterior;

    for(int i=start;i<=endd;i++)
    {
        posterior=p_gauss(minTemp[i],maxTemp[i],humidity[i])*classifiar_prob;
        if(posterior>=0.5 && raintomorrow[i]==1)
            accuracy_cnt++;
        else if(posterior<0.5 && raintomorrow[i]==0)
            accuracy_cnt++;
    }
    //cout<<accuracy_cnt<<endl;
    return (accuracy_cnt/(double)99);
}

double kfold(int turn)
{

    initialize_new_vector(turn);
    train(turn);

    double ret_value =  test(turn);
    //cout<<ret_value;
    return ret_value;

}
int main() {
  ifstream file("Book1.csv");
  string line;
  int sz=0;

  while (getline(file, line))
    {
    vector<std::string> values;
    istringstream line_stream(line);
    string value;

    while (getline(line_stream, value, ','))
        {
      values.push_back(value);
        }

        minTemp.push_back(stod(values[0]));
        maxTemp.push_back(stod(values[1]));
        humidity.push_back(stod(values[2]));

        if(values[3]=="Yes")
            cloud9am.push_back(1);
        else
            cloud9am.push_back(0);

        if(values[4]=="Yes")
            cloud3pm.push_back(1);
        else
            cloud3pm.push_back(0);

        if(values[5]=="Yes")
            raintoday.push_back(1);
        else
            raintoday.push_back(0);

        if(values[6]=="Yes")
            raintomorrow.push_back(1);
        else
            raintomorrow.push_back(0);

        sz++;

    /*for (const auto& val : values) {
      cout << val << "\t";
    }
    cout << "\n";*/
  }

  /*for(int i=0;i<sz;i++)
  {
      cout<<minTemp[i]<<"\t"<<maxTemp[i]<<"\t"<<humidity[i]<<"\t"<<cloud9am[i]<<"\t"<<cloud3pm[i]<<"\t"<<raintoday[i]<<"\t"<<raintomorrow[i]<<endl;
  }*/

  cout<<endl;
  classifiar_prob=p_classifiar();
//cout<<classifiar_prob;
  double acc,k_val;

  for(int i=0;i<10;i++)
  {
      k_val=kfold(i);
      acc=acc+k_val;
      cout<<"Round :"<<i+1<<" Accuracy: "<<k_val*100<<"%"<<endl;
  }

  cout<<endl<<endl<<"Final Accuracy: "<<(acc/(double)10)*(double)100<<"%"<<endl<<endl;
  return 0;
}

double p_minTemp_yes(double input)
{
    double section1, section2, etothepower;
    section1 = (1/(sqrt(2*3.1416*minTemp_vari)));
    section2 = -1*((input-minTemp_mean)*(input-minTemp_mean))/(2*minTemp_vari);
    etothepower = exp(section2);


    return section1*etothepower;
}

double p_maxTemp_yes(double input)
{
    double section1, section2, etothepower;
    section1 = (1/(sqrt(2*3.1416*maxTemp_vari)));
    section2 = -1*((input-maxTemp_mean)*(input-maxTemp_mean))/(2*maxTemp_mean);
    etothepower = exp(section2);


    return section1*etothepower;
}

double p_humidity_yes(double input)
{
    double section1, section2, etothepower;
    section1 = (1/(sqrt(2*3.1416*humidity_vari)));
    section2 = -1*((input-humidity_vari)*(input-humidity_mean))/(2*humidity_vari);
    etothepower = exp(section2);


    return section1*etothepower;
}


double p_gauss(double mintemp,double maxtemp, double humid)
{
    return (p_maxTemp_yes(maxtemp)*p_minTemp_yes(mintemp)*p_humidity_yes(humid));
}
