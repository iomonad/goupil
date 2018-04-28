defmodule SnatLogInterface do

  @doc "Custom loggin interface
  for the application"
  @spec format(level :: integer,
	message :: String.t,
	timestamp :: String.t,
	metadata :: String.t) :: none
  def format(level, message, timestamp, metadata) do
	# todo: add pretty printer to a file.
	IO.puts message
  end
end
