
find . -name *.java | xargs perl -p -i -e "BEGIN{undef $/;} s|// SCHNIPP.*?// SCHNAPP||smg"
