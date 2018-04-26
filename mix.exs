defmodule Snat.MixProject do
  use Mix.Project

  def project do
    [
      app: :snat,
      version: "0.1.0",
      elixir: "~> 1.6",
      start_permanent: Mix.env() == :prod,
	  test_coverage: [tool: ExCoveralls],
	  preferred_cli_env: ["coveralls": :test, "coveralls.detail": :test, "coveralls.post": :test, "coveralls.html": :test],
      deps: deps()
    ]
  end

  def application do
    [
      extra_applications: [:logger]
    ]
  end

  defp deps do
    [
	  {:excoveralls, "~> 0.8", only: :test}
    ]
  end
end
