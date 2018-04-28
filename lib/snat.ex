defmodule Snat do
  use Slack

  @doc "Start our RTM link"
  @spec main :: none
  def main do
	case Application.get_env(:snat, :slack_token) do
	  nil -> Process.exit(self(), :normal)
	  token -> IO.puts("Running with legacy token: #{token}")
	end
	{:ok, rtm} = Slack.Bot.start_link(SlackRtm, [],
	  Application.get_env(:snat, :slack_token))
	send rtm, {:message, "External message", "@Citrouille"}
  end
end
