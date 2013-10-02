package comeon;

import org.kohsuke.args4j.Option;

final class Arguments {
  @Option(name = "--rescue", required = false, usage = "args.rescue")
  private Boolean rescue = false;
  
  public Boolean getRescue() {
    return rescue;
  }
  
  public void setRescue(final Boolean rescue) {
    this.rescue = rescue;
  }
}
