#Move this script to the desired place, i personally put it on /root/ directory of my debian so it is available when login to terminal.
echo "Clear OLD servers"
tmux list-session
tmux kill-session -t airline-data
tmux kill-session -t airline-web
tmux list-session
echo "Starting Data Server"
tmux new-session -d -s airline-data
tmux select-window -t airline-data:
tmux send-keys "cd /var/www/flight.jcf.al/airline/airline-data/" Enter
tmux send-keys "./start-data.sh" Enter
echo "Waiting 20 seconds before starting Web Server"
sleep 20
tmux new-session -d -s airline-web
tmux select-window -t airline-web:0
tmux send-keys "cd /var/www/flight.jcf.al/airline/airline-web/" Enter
tmux send-keys "sbt run" Enter
echo "Both server being started, closing."
echo "NB : Check process list to verify."
exit